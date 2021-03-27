package com.example.dtmuz.ui.firstActivityFragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dtmuz.R
import com.example.dtmuz.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {



    private lateinit var viewModel: RegistrationViewModel
    private lateinit var binding: RegistrationFragmentBinding
    private var number: String? = null
    private var smsCode: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = RegistrationViewModelFactory(requireActivity())
        viewModel = ViewModelProvider(this, factory).get(RegistrationViewModel::class.java)
        setupAction()
    }


    private fun setupAction() {
        binding.signInBtn.setOnClickListener {
            if (viewModel.isSignIn) {
                if (checkNumber()) {
                    viewModel.sendVerificationCode(binding.editNumber.text.toString()).observe(viewLifecycleOwner){smsCode ->
                        this.smsCode = smsCode
                        binding.smsCode.setText(smsCode)
                    }
                    signIn()
                }

            } else{
                if (checkPin()){
                    signInUser()

                }

            }
        }
    }



    private fun signInUser(){
        viewModel.signInUser(smsCode).observe(viewLifecycleOwner) {response ->
          when(response){
              LoginResponse.SUCCESS -> {
                  Toast.makeText(activity, "sign in success ", Toast.LENGTH_LONG).show()
                  findNavController().navigate(R.id.secondActivity)

              }
              LoginResponse.CANCELLED -> {
                  Toast.makeText(activity, "sign in cancelled ", Toast.LENGTH_LONG).show()

              }
              LoginResponse.FAILURE -> {
                  Toast.makeText(activity, "sign in failure", Toast.LENGTH_LONG).show()

              }
              else -> {

              }
           }
        }
    }

    private fun signIn() {
        binding.editNumber.visibility = View.GONE
        binding.smsCode.visibility = View.VISIBLE
        binding.signInBtn.text = "Registration"
        viewModel.isSignIn=false

    }

    private fun checkNumber(): Boolean {
        number = binding.editNumber.text.toString().trim()
        if (number!!.isEmpty()) {
            binding.editNumber.error = "maydonni to'ldiring"
            return false
        } else if (number!!.length < 13) {
            binding.editNumber.error = "raqam uzunligi 13 bo'lishi kerak"
            return false
        } else return true
    }

    private fun checkPin(): Boolean {
        smsCode = binding.smsCode.text.toString()
        return when {
            smsCode.isEmpty() -> {
                binding.smsCode.error = "maydonni to'ldiring"
                false
            }
            smsCode.length < 6 -> {
                binding.smsCode.error = "cod uzunligi 6 bo'lishi kerak"
                false
            }
            else -> true
        }
    }

}