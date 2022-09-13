package org.itmo.mop.animalmap.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import javax.inject.Inject

class MapsViewModel @Inject constructor(private val coordinatesRepository: CoordinatesRepository): ViewModel() {

    private val _state: MutableStateFlow<MapsViewModelState> = MutableStateFlow(MapsViewModelState())
    val state: StateFlow<MapsViewModelState>
    get() = _state

    fun onMapReady() {
        loadCoordinates()
    }

    private fun loadCoordinates() {
        viewModelScope.launch {
            val coordinates = coordinatesRepository.getAllCoordinates()
            updateState {
                copy(isLoading = false, coordinates = coordinates)
            }
        }
    }

    //TODO: MOVE
    private fun updateState(transform: MapsViewModelState.() -> MapsViewModelState) {
        _state.value = transform.invoke(state.value)
    }
}

data class MapsViewModelState(
    val isLoading: Boolean = true,
    val coordinates: List<AnimalCoordinate> = emptyList()
)
