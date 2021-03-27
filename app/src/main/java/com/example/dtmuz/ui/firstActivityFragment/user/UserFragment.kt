package com.example.dtmuz.ui.firstActivityFragment.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dtmuz.R
import com.example.dtmuz.databinding.UserFragmentBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.IntentUtils
import java.io.File

class UserFragment : Fragment() {
    private val TAG="UserFragment"
    private lateinit var binding: UserFragmentBinding

    companion object {
        private const val PROFILE_IMAGE_REQ_CODE = 101
        private const val GALLERY_IMAGE_REQ_CODE = 102
        private const val CAMERA_IMAGE_REQ_CODE = 103
    }

    private lateinit var viewModel: UserViewModel
    private var mProfileFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container, false)
        binding.imgProfile.setDrawableImage(R.drawable.ic_user_line,true)
        binding.fabAddPhoto.setOnClickListener {
            pickProfileImage(requireView())
        }
        binding.imgProfile.setOnClickListener {
            showImage(requireView())
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
    }

  fun pickProfileImage(view: View) {
        ImagePicker.with(requireActivity())
            .cropSquare()
            .setImageProviderInterceptor {
                Log.d("ImagePicker", "Selected ImageProvider: " + it.name)
            }
            .setDismissListener {
                Log.d("ImagePicker", "Dialog Dismiss")

            }
            .maxResultSize(512, 512)
            .start(PROFILE_IMAGE_REQ_CODE)
    }


    private fun ImageView.setDrawableImage(@DrawableRes resource: Int, applyCircle: Boolean = false) {
        val glide = Glide.with(this).load(resource)
        if (applyCircle) {
            glide.apply(RequestOptions.circleCropTransform()).into(this)
        } else {
            glide.into(this)
        }
    }

    private fun ImageView.setLocalImage(file: File, applyCircle: Boolean = false) {
        val glide = Glide.with(this).load(file)
        if (applyCircle) {
            glide.apply(RequestOptions.circleCropTransform()).into(this)
        } else {
            glide.into(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val file = ImagePicker.getFile(data)!!
            Log.d(TAG, "onActivityResult: $file")
            when (requestCode) {
                PROFILE_IMAGE_REQ_CODE -> {
                    mProfileFile = file
                    binding.imgProfile.setLocalImage(file, true)
                    Log.d(TAG, "onActivityResult:$file ")

                }

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
     fun showImage(view: View) {
        val file = when (view) {
           binding.imgProfile -> mProfileFile
            else -> null
        }

        file?.let {
            IntentUtil.showImage(requireActivity(), file)
        }


    }

}