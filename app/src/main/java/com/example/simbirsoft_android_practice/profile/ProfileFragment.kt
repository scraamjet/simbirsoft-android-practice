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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentProfileBinding
import com.example.simbirsoft_android_practice.model.Friend
import dev.androidbroadcast.vbpd.viewBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val friendAdapter by lazy { FriendAdapter() }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let { capturedImage -> updateAppBarImageFromCamera(capturedImage) }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { selectedImage -> updateAppBarImageFromGallery(selectedImage) }
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
    }

    private fun initRecyclerView() {
        binding.recyclerViewFriends.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendAdapter
        }
        val list: List<Friend> =
            listOf(
                Friend(
                    1,
                    "Алексис Санчес",
                    "https://photobooth.cdn.sports.ru/preset/tc_person/4/02/2c8b043f747e8b03764db15fc1d2d.png",
                ),
                Friend(
                    2,
                    "Деклан Райс",
                    "https://photobooth.cdn.sports.ru/preset/tags/3/1a/c964ab3eb44d883cca720b243570a.png",
                ),
                Friend(
                    3,
                    "Букайо Сака",
                    "https://photobooth.cdn.sports.ru/preset/tc_person/a/8b/0e7d6eba2431fa68d0275d1124d82.jpeg",
                ),
                Friend(
                    4,
                    "Алексей Гладков",
                    "https://thumb.tildacdn.com/tild3739-3337-4530-b562-643539663265/-/format/webp/_.jpg",
                ),
                Friend(
                    5,
                    "Кирилл Розов",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNtxwOwoQCubf4BzQpq4erjTloyf3O2uUblg&s",
                ),
                Friend(
                    6,
                    "Райан Гослинг",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_Gn1Em872bptcqUX2Yytct8--VEYCUv3kwQ&s",
                ),
            )
        friendAdapter.submitList(list)
    }

    private fun listenToPhotoDialog() {
        parentFragmentManager.setFragmentResultListener(
            EditPhotoDialogKeys.REQUEST_KEY,
            viewLifecycleOwner,
        ) { _, bundle ->
            val actionName = bundle.getString(EditPhotoDialogKeys.ACTION_KEY)
            val action = PhotoAction.valueOf(actionName ?: return@setFragmentResultListener)
            handlePhotoAction(action)
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
