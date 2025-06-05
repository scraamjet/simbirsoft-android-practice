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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.ProfileViewModel
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentProfileBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val friendAdapter by lazy { FriendAdapter() }

    private val viewModel: ProfileViewModel by viewModels()

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { cameraBitmap -> viewModel.setCameraImage(cameraBitmap) }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { imageUri -> viewModel.setGalleryImage(imageUri) }
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

        binding.appBarImageProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_edit_photo_dialog)
        }

        initRecyclerView()
        listenToPhotoDialog()
        collectViewModel()
    }

    private fun initRecyclerView() {
        binding.recyclerViewFriends.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendAdapter
        }
    }

    private fun listenToPhotoDialog() {
        parentFragmentManager.setFragmentResultListener(
            EditPhotoDialogKeys.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val actionName = bundle.getString(EditPhotoDialogKeys.ACTION_KEY)
            val action = PhotoAction.valueOf(actionName ?: return@setFragmentResultListener)
            viewModel.onPhotoActionSelected(action)
        }
    }

    private fun collectViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.friends.collect { friends -> friendAdapter.submitList(friends) }
                }

                launch {
                    viewModel.photoAction.collect { action -> handlePhotoAction(action) }
                }

                launch {
                    viewModel.galleryImageUri.collect { uri -> updateAppBarImageFromGallery(uri) }
                }

                launch {
                    viewModel.cameraImageBitmap.collect { bitmap ->
                        updateAppBarImageFromCamera(
                            bitmap
                        )
                    }
                }
            }
        }
    }

    private fun handlePhotoAction(action: PhotoAction) {
        when (action) {
            PhotoAction.TAKE_PHOTO -> handleTakePhoto()
            PhotoAction.CHOOSE_PHOTO -> handleChoosePhoto()
            PhotoAction.DELETE_PHOTO -> clearAppBarImage()
        }
    }

    private fun handleTakePhoto() {
        when {
            hasCameraPermission() -> cameraLauncher.launch()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showSettingsDialog()
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun handleChoosePhoto() {
        galleryLauncher.launch("image/*")
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

    private fun hasCameraPermission(): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            )
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }

    private fun updateAppBarImageFromCamera(bitmap: Bitmap) {
        binding.appBarImageProfile.apply {
            setImageBitmap(bitmap)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun updateAppBarImageFromGallery(uri: Uri) {
        binding.appBarImageProfile.apply {
            setImageURI(uri)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun clearAppBarImage() {
        binding.appBarImageProfile.apply {
            setImageResource(R.drawable.user_icon)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }
}
