package com.example.simbirsoft_android_practice.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.domain.usecase.EventsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<EventListEvent>(extraBufferCapacity = 1)

        private val _state = MutableStateFlow<EventListState>(EventListState.Loading)
        val state: StateFlow<EventListState> = _state.asStateFlow()

        init {
            observeEvents()
        }

        fun onEvent(event: EventListEvent) {
            viewModelScope.launch {
                _eventFlow.emit(event)
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun observeEvents() {
            viewModelScope.launch {
                _eventFlow
                    .filterIsInstance<EventListEvent.SearchQueryChanged>()
                    .flatMapLatest { event ->
                        flow {
                            eventsUseCase(event.query)
                                .map { events -> Pair(events, event.query) }
                                .catch {
                                    emit(EventListState.Error)
                                }
                                .collect { (events, query) ->
                                    val eventState =
                                        when {
                                            query.isBlank() -> EventListState.Idle
                                            events.isEmpty() -> EventListState.Empty
                                            else -> EventListState.Result(events)
                                        }
                                    emit(eventState)
                                }
                        }
                    }
                    .collect { eventState ->
                        _state.value = eventState
                    }
            }
        }
    }
