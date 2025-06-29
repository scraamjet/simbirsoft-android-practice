package com.example.profile.presentation.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes

sealed class ProfileEffect {
    data class PhotoAction(val action: com.example.profile.domain.model.PhotoAction) : ProfileEffect()
    data class GalleryImage(val uri: Uri) : ProfileEffect()
    data class CameraImage(val bitmap: Bitmap) : ProfileEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : ProfileEffect()
}
