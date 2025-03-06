package com.example.simbirsoft_android_practice

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.simbirsoft_android_practice.databinding.FragmentProfileBinding
import dev.androidbroadcast.vbpd.viewBinding

private const val IMAGE_SELECTOR_TAG = "IMAGE_SELECTOR_TAG"

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            handleCameraPermissionResult(isGranted)
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { updateAppBarImage(it) }
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        handleBackPress()
    }

    private fun initUI() {
        binding.appBarImage.setOnClickListener {
            showEditPhotoDialog()
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            },
        )
    }

    private fun showEditPhotoDialog() {
        EditPhotoDialogFragment.newInstance { action ->
            when (action) {
                PhotoAction.TAKE_PHOTO -> handleTakePhoto()
                PhotoAction.DELETE_PHOTO -> clearAppBarImage()
            }
        }.show(childFragmentManager, IMAGE_SELECTOR_TAG)
    }

    private fun handleTakePhoto() {
        when {
            hasCameraPermission() -> launchCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showCameraPermissionWarning()
            else -> requestCameraPermission()
        }
    }

    private fun launchCamera() {
        cameraLauncher.launch()
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun handleCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            launchCamera()
        } else {
            showCameraPermissionWarning()
        }
    }

    private fun hasCameraPermission(): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            )
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }

    private fun showCameraPermissionWarning() {
        Toast.makeText(
            requireContext(),
            R.string.profile_camera_permission_warning,
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun updateAppBarImage(bitmap: Bitmap) {
        binding.appBarImage.setImageBitmap(bitmap)
    }

    private fun clearAppBarImage() {
        binding.appBarImage.setImageResource(R.drawable.user_icon)
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
