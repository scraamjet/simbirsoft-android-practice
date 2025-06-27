package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Load -> loadFriends()
            is ProfileEvent.PhotoActionSelected -> handleAction(ProfileEffect.PhotoAction(event.action))
            is ProfileEvent.SetGalleryImage -> handleAction(ProfileEffect.GalleryImage(event.uri))
            is ProfileEvent.SetCameraImage -> handleAction(ProfileEffect.CameraImage(event.bitmap))
        }
    }

    private fun loadFriends() {
        viewModelScope.launch {
            try {
                val friends = profileUseCase.loadFriends()
                _state.value = ProfileState.Result(friends)
            } catch (exception: Exception) {
                _state.value = ProfileState.Error
                _effect.emit(ProfileEffect.ShowErrorToast(R.string.profile_load_error))
            }
        }
    }

    private fun handleAction(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
