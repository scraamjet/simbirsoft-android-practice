package com.example.simbirsoft_android_practice.presentation.profile

import android.graphics.Bitmap
import android.net.Uri

sealed class ProfileEffect {
    data class SelectedPhotoAction(val action: PhotoAction) : ProfileEffect()
    data class GalleryImage(val uri: Uri) : ProfileEffect()
    data class CameraImage(val bitmap: Bitmap) : ProfileEffect()
    data object ShowErrorToast : ProfileEffect()
}
