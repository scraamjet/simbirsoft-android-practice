package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.Manifest
import androidx.activity.result.launch


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var profileImageView: ImageView

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> launchCamera()
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showCameraPermissionWarning()
                else -> showCameraPermissionWarning()
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { profileImageView.setImageBitmap(it) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileImageView = view.findViewById(R.id.app_bar_image)

        profileImageView.setOnClickListener {
            EditPhotoDialogFragment.newInstance().show(childFragmentManager, IMAGE_SELECTOR)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
        )
    }

    private fun launchCamera() {
        cameraLauncher.launch()
    }

    fun takePhotoFromCamera() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showCameraPermissionWarning()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showCameraPermissionWarning() {
        Toast.makeText(
            requireContext(),
            resources.getText(R.string.nav_news),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val IMAGE_SELECTOR = "IMAGE_SELECTOR"

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}