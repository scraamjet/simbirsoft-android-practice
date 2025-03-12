package com.example.simbirsoft_android_practice.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentProfileBinding
import dev.androidbroadcast.vbpd.viewBinding

private const val IMAGE_SELECTOR_TAG = "IMAGE_SELECTOR_TAG"

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { capturedImage -> updateAppBarImage(capturedImage) }
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch()
            } else {
                showSettingsDialog()
            }
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarImage.setOnClickListener { showEditPhotoDialog() }
        handleBackPress()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
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
        if (hasCameraPermission()) {
            cameraLauncher.launch()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.permission_required)
            .setMessage(R.string.camera_permission_settings_message)
            .setPositiveButton(R.string.open_settings) { _, _ -> openAppSettings() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun openAppSettings() {
        startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            },
        )
    }

    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

    private fun updateAppBarImage(bitmap: Bitmap) {
        binding.appBarImage.apply {
            setImageBitmap(bitmap)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun clearAppBarImage() {
        binding.appBarImage.apply {
            setImageResource(R.drawable.user_icon)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
