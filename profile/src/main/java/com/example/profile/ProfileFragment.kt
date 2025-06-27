package com.example.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profile.databinding.FragmentProfileBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val friendAdapter by lazy { FriendAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                viewModel.onEvent(ProfileEvent.SetCameraImage(bitmap))
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.onEvent(ProfileEvent.SetGalleryImage(uri))
            }
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch()
            } else {
                showSettingsDialog()
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val profileComponent = context.appComponent
            .profileComponentFactory()
            .create(context)
        profileComponent.inject(this)
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onEvent(ProfileEvent.Load)

        binding.appBarImageProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileToEditPhotoDialog()
            findNavController().navigate(action)
        }

        initRecyclerView()
        listenToPhotoDialog()
        observeState()
        observeEffect()
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
            viewLifecycleOwner,
        ) { _, resultBundle ->
            val actionName = resultBundle.getString(EditPhotoDialogKeys.ACTION_KEY)
            val photoAction = PhotoAction.valueOf(actionName ?: return@setFragmentResultListener)
            viewModel.onEvent(ProfileEvent.PhotoActionSelected(photoAction))
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ProfileState.Idle -> Unit
                        is ProfileState.Result -> showFriends(state.friends)
                        is ProfileState.Error -> hideContentOnError()
                    }
                }
            }
        }
    }

    private fun observeEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is ProfileEffect.PhotoAction -> handlePhotoAction(effect.action)
                        is ProfileEffect.GalleryImage -> updateAppBarImageFromGallery(effect.uri)
                        is ProfileEffect.CameraImage -> updateAppBarImageFromCamera(effect.bitmap)
                        is ProfileEffect.ShowErrorToast -> showToast(effect.messageResId)
                    }
                }
            }
        }
    }

    private fun showFriends(friends: List<Friend>) {
        friendAdapter.submitList(friends)
    }

    private fun hideContentOnError() {
        friendAdapter.submitList(emptyList())
    }

    private fun showToast(
        @StringRes messageResId: Int,
    ) {
        Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
    }

    private fun handlePhotoAction(action: PhotoAction) {
        when (action) {
            PhotoAction.TAKE_PHOTO -> handleTakePhoto()
            PhotoAction.CHOOSE_PHOTO -> galleryLauncher.launch("image/*")
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

    private fun hasCameraPermission(): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            )
        return permissionStatus == PackageManager.PERMISSION_GRANTED
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

    private fun updateAppBarImageFromGallery(uri: Uri) {
        binding.appBarImageProfile.apply {
            setImageURI(uri)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun updateAppBarImageFromCamera(bitmap: Bitmap) {
        binding.appBarImageProfile.apply {
            setImageBitmap(bitmap)
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
