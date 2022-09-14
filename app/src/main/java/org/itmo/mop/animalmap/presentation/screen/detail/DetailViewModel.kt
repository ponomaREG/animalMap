package org.itmo.mop.animalmap.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import org.itmo.mop.animalmap.presentation.base.BaseViewModel
import org.itmo.mop.animalmap.presentation.base.Event

class DetailViewModel @AssistedInject constructor(
    private val coordinatesRepository: CoordinatesRepository,
    @Assisted private val animalId: Int,
) : BaseViewModel<DetailState, DetailEvent>() {

    override val initialState: DetailState
        get() = DetailState()

    init {
        loadInfo()
    }

    private fun loadInfo() {
        viewModelScope.launch {
            val animalInfo = coordinatesRepository.getAnimalInfoBy(animalId)
            updateState {
                copy(isLoading = false, animalInfo = animalInfo)
            }
        }
    }

    @AssistedFactory
    interface DetailViewModelAssistedFactory {
        fun create(animalId: Int): DetailViewModel
    }

    companion object {

        fun providesFactory(
            assistedFactory: DetailViewModelAssistedFactory,
            animalId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(animalId) as T
            }
        }
    }

}

data class DetailState(
    val isLoading: Boolean = true,
    val animalInfo: AnimalCoordinate? = null
)

sealed class DetailEvent : Event