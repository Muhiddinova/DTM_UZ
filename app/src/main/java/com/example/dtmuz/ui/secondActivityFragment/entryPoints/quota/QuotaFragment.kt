package com.example.dtmuz.ui.secondActivityFragment.entryPoints.quota

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dtmuz.R

class QuotaFragment : Fragment() {

    companion object {
        fun newInstance() = QuotaFragment()
    }

    private lateinit var viewModel: QuotaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.quota_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QuotaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}