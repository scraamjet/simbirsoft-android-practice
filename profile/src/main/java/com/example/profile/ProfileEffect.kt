package com.example.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes

sealed class ProfileEffect {
    data class PhotoAction(val action: com.example.profile.PhotoAction) : ProfileEffect()
    data class GalleryImage(val uri: Uri) : ProfileEffect()
    data class CameraImage(val bitmap: Bitmap) : ProfileEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : ProfileEffect()
}
