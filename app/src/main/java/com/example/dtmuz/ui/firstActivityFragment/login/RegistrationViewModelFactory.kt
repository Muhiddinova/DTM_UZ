package com.example.dtmuz.ui.firstActivityFragment.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegistrationViewModelFactory( private val activity: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)){
            return RegistrationViewModel(activity) as T
        } else{
            throw IllegalArgumentException("RegistrationViewModel not found")
        }
    }}