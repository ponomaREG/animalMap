package org.itmo.mop.animalmap.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T, E : Event> : ViewModel() {

    abstract val initialState: T

    private val _state: MutableStateFlow<T> = MutableStateFlow(initialState)
    val state: StateFlow<T>
        get() = _state

    private val _event: MutableSharedFlow<E> = MutableSharedFlow(
        replay = 0,
    )
    val event: SharedFlow<E>
        get() = _event

    protected fun updateState(transform: T.() -> T) {
        _state.value = transform.invoke(state.value)
    }

    protected fun submitEvent(userEvent: E) {
        viewModelScope.launch {
            _event.emit(userEvent)
        }
    }
}

interface Event