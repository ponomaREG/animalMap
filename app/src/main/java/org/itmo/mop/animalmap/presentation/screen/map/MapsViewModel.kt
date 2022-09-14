package org.itmo.mop.animalmap.presentation.screen.map

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import org.itmo.mop.animalmap.presentation.base.BaseViewModel
import org.itmo.mop.animalmap.presentation.base.Event
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val coordinatesRepository: CoordinatesRepository) :
    BaseViewModel<MapsViewModelState, MapsEvent>() {

    override val initialState: MapsViewModelState
        get() = MapsViewModelState()

    private var periodicWork: Job? = null

    fun onMapReady() {
        loadCoordinates()
    }

    fun onMarkerClicked(markerId: Int) {
        submitEvent(MapsEvent.ShowDetailActivity(markerId))
    }

    fun onAddAnimalClicked() {
        submitEvent(MapsEvent.ShoAddAnimalActivity)
    }

    fun onNewAnimalAdded(animalCoordinate: AnimalCoordinate) {
        updateState {
            copy(
                coordinates = coordinates.toMutableList().also { it.add(animalCoordinate) }
            )
        }
    }

    fun onStartAndMapReady() {
        loadCoordinates()
    }

    fun onStop() {
        periodicWork?.cancel()
    }

    private fun loadCoordinates() {
        periodicWork = viewModelScope.launch {
            while (isActive) {
                val coordinates = coordinatesRepository.getAllCoordinates()
                updateState {
                    copy(isLoading = false, coordinates = coordinates)
                }
                delay(10000L)
            }
        }
    }


}

data class MapsViewModelState(
    val isLoading: Boolean = true,
    val coordinates: List<AnimalCoordinate> = emptyList()
)

sealed class MapsEvent : Event {
    class ShowDetailActivity(val animalId: Int) : MapsEvent()
    object ShoAddAnimalActivity : MapsEvent()
}
