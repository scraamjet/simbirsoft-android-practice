package com.example.simbirsoft_android_practice.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.domain.usecase.ProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Success(emptyList()))
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Load -> loadFriends()
            is ProfileEvent.PhotoActionSelected -> emitEffect(ProfileEffect.HandlePhotoAction(event.action))
            is ProfileEvent.SetGalleryImage -> emitEffect(ProfileEffect.GalleryImage(event.uri))
            is ProfileEvent.SetCameraImage -> emitEffect(ProfileEffect.CameraImage(event.bitmap))
        }
    }

    private fun loadFriends() {
        viewModelScope.launch {
            try {
                val friends = profileUseCase.loadFriends()
                _state.value = ProfileState.Success(friends)
            } catch (exception: Exception) {
                _state.value = ProfileState.Error
            }
        }
    }

    private fun emitEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}

