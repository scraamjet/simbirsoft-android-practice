package com.example.simbirsoft_android_practice.profile

import android.graphics.Bitmap
import android.net.Uri

sealed class ProfileEvent {
    object Load : ProfileEvent()
    data class PhotoActionSelected(val action: PhotoAction) : ProfileEvent()
    data class SetGalleryImage(val uri: Uri) : ProfileEvent()
    data class SetCameraImage(val bitmap: Bitmap) : ProfileEvent()
}