package com.example.simbirsoft_android_practice.domain

import com.example.core.usecase.StartNewsServiceUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class StartNewsServiceUseCaseImpl @Inject constructor() : StartNewsServiceUseCase {
    private val _requests = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override suspend fun requestStart() {
        _requests.emit(Unit)
    }

    override fun observeRequests(): SharedFlow<Unit> {
        return _requests.asSharedFlow()
    }
}