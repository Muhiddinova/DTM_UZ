package com.example.dtmuz.repository

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dtmuz.model.User
import com.example.dtmuz.ui.firstActivityFragment.login.LoginResponse
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

@Suppress("NAME_SHADOWING")
class RegistrationRepository(private var firebaseAuth: FirebaseAuth, private var activity: Activity) {

    private val TAG = "RegistrationRepository"
    private var verificationId: String? = null
    val verificationLiveData = MutableLiveData<String?>()

    companion object {
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        private var INSTANCE: RegistrationRepository? = null
        private var databaseReference: DatabaseReference? = null
        fun getInstance(activity: Activity): RegistrationRepository {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            firebaseAuth = FirebaseAuth.getInstance()
            databaseReference = FirebaseDatabase.getInstance().getReference("User")
            INSTANCE = RegistrationRepository(firebaseAuth, activity)
            return INSTANCE!!

        }


        }
    fun sendVerificationCode(phone:String): LiveData<String> {
        val mSmsCode = MutableLiveData<String>()
        val options = PhoneAuthOptions.newBuilder()
            .setActivity(activity)
            .setTimeout(60,TimeUnit.SECONDS)
            .setPhoneNumber(phone)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//                    verificationLiveData.value=verificationId
                    this@RegistrationRepository.verificationId=verificationId
                    super.onCodeSent(verificationId, token)
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    var smsCode = credential.smsCode
                    if (smsCode != null) {
                        mSmsCode.value  = smsCode
//                        val credential =
//                            PhoneAuthProvider.getCredential(checkNotNull(verificationId!!), smsCode)
//                        signInWithCredential(credential)
//                        Toast.makeText(activity, "$smsCode", Toast.LENGTH_SHORT).show()
//                        Log.d(TAG, "onVerificationCompleted: $smsCode")
                    }
                    else{
                        smsCode=""

                    }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    Toast.makeText(
                        activity,
                        "onVerification failure $exception",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
return mSmsCode
    }
    fun signInWithCredential(smsCode:String) : LiveData<LoginResponse>{
        val loginResponse = MutableLiveData<LoginResponse>()
        val credentials = PhoneAuthProvider.getCredential(verificationId?:"",smsCode)
        Companion.firebaseAuth.signInWithCredential(credentials)
            .addOnSuccessListener {
                val user =
                    User(
                        Companion.firebaseAuth.uid!!, "", Companion.firebaseAuth.currentUser!!.phoneNumber!!, "",
                        arrayListOf()
                    )
                loginResponse.value = LoginResponse.SUCCESS
                databaseReference!!.child(Companion.firebaseAuth.uid!!).setValue(user)
            }
            .addOnFailureListener {
                Toast.makeText(activity, "sign in failure $it", Toast.LENGTH_LONG).show()
                loginResponse.value = LoginResponse.FAILURE
            }
            .addOnCanceledListener {
                Toast.makeText(activity, "sign in cancelled ", Toast.LENGTH_LONG).show()
                loginResponse.value = LoginResponse.CANCELLED
            }
        return loginResponse
    }
}