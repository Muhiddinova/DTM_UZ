package com.example.dtmuz.ui.firstActivityFragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.dtmuz.R
import com.example.dtmuz.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SplashFragment : Fragment() {
    private lateinit var timer:Timer
    private lateinit var binding:FragmentSplashBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding=DataBindingUtil.inflate(inflater,R.layout.fragment_splash, container, false)
        return binding.root
    }
    private fun waitAndOpenOtherFragment(){
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        timer= Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                if (firebaseAuth.currentUser!=null){
                    findNavController().navigate(R.id.secondActivity)
                }else{
                    findNavController().navigate(R.id.userFragment)
                }
            }



        },4000)
    }

    override fun onResume() {
        super.onResume()
        waitAndOpenOtherFragment()
    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }


}