package com.example.dtmuz.ui.firstActivityFragment.login

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dtmuz.repository.RegistrationRepository

class RegistrationViewModel(activity: Activity) : ViewModel() {
    private val TAG = "RegistrationViewModel"
    private var repo: RegistrationRepository = RegistrationRepository.getInstance(activity)
    var phone:String? = null
    var smsCode:String? = null

     var isSignIn=true

    fun onSignInButtonClicked(view: View){

        if (!isSignIn){
            Log.e(TAG, "signInFailure: $view")
            sendVerificationCode(phone!!)
        } else{
            signInUser(smsCode!!)
        }
    }
    fun sendVerificationCode(phone: String): LiveData<String> {

        return repo.sendVerificationCode(phone)
    }
    fun signInUser(smsCode: String): LiveData<LoginResponse> {
        return repo.signInWithCredential(smsCode)
    }
}