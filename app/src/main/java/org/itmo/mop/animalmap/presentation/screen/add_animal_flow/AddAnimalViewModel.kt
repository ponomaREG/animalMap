package org.itmo.mop.animalmap.presentation.screen.add_animal_flow

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.data.persistence.TokenPersistence
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import org.itmo.mop.animalmap.presentation.base.BaseViewModel
import org.itmo.mop.animalmap.presentation.base.Event
import retrofit2.HttpException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddAnimalViewModel @Inject constructor(
    private val coordinatesRepository: CoordinatesRepository,
    private val tokenPersistence: TokenPersistence,
) : BaseViewModel<AddAnimalState, AddAnimalEvent>() {

    override val initialState: AddAnimalState
        get() = AddAnimalState()

    fun onButtonNextPageClicked() = with(state.value) {
        val event = when (currentStateFlow) {
            AddAnimalCurrentStateFlow.Name ->
                AddAnimalEvent.ShowNextPage(R.id.action_add_flow_name_to_add_flow_photo)
            AddAnimalCurrentStateFlow.Photo ->
                AddAnimalEvent.ShowNextPage(R.id.action_add_flow_photo_to_add_flow_position)
            else -> TODO()
        }
        submitEvent(event)
    }

    fun onButtonSkipClicked() {
        submitEvent(AddAnimalEvent.ShowNextPage(R.id.action_add_flow_photo_to_add_flow_position))
    }

    fun onMapReady() {
        updateState {
            copy(
                isLoading = false
            )
        }
    }

    fun onAddAnimalNameShow() {
        updateState {
            copy(
                currentStateFlow = AddAnimalCurrentStateFlow.Name,
                isButtonNextPageEnabled = name.isNotEmpty() && description.isNotEmpty()
            )
        }
    }

    fun onAddAnimalPhotoShow() {
        updateState {
            copy(
                currentStateFlow = AddAnimalCurrentStateFlow.Photo,
                isButtonNextPageEnabled = false
            )
        }
    }

    fun onAddAnimalPositionShow() {
        updateState {
            copy(
                currentStateFlow = AddAnimalCurrentStateFlow.Position,
                isButtonNextPageEnabled = latLng != null,
                isLoading = true
            )
        }
    }

    fun onButtonSaveAnimalClicked() = with(state.value) {
        viewModelScope.launch {
            //TODO: Ленивая обработка ошибок, желательно вынести это все в слой даты
            try {
                val newMarkerId = coordinatesRepository.addAnimal(
                    name = name,
                    description = description,
                    latitude = latLng!!.latitude,
                    longitude = latLng.longitude,
                    photo = imageStream
                )
                if (newMarkerId < 0) submitEvent(AddAnimalEvent.ShowToast("Не удалось добавить!"))
                submitEvent(AddAnimalEvent.FinishFlow)
            } catch (h: HttpException) {
                val event: AddAnimalEvent = if (h.code() == 403) {
                    tokenPersistence.clearImmediately()
                    AddAnimalEvent.GoToAuthGraph
                }  else AddAnimalEvent.ShowToast("Не удалось добавить!")
                submitEvent(event)
            }
        }
    }

    fun onButtonPickPhotoClicked() {
        submitEvent(AddAnimalEvent.OpenPhotoPicker)
    }

    fun onNameChanged(name: String) {
        updateState {
            copy(
                name = name,
                isButtonNextPageEnabled = name.isNotEmpty() && description.isNotEmpty()
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        updateState {
            copy(
                description = description,
                isButtonNextPageEnabled = name.isNotEmpty() && description.isNotEmpty()
            )
        }
    }

    fun onCoordinatesChanged(latLng: LatLng) {
        updateState {
            copy(
                latLng = latLng,
                isButtonNextPageEnabled = true
            )
        }
    }

    fun onPhotoPicked(photoUri: Uri, photoStream: InputStream) {
        updateState {
            copy(
                imageUri = photoUri,
                imageStream = photoStream,
                isButtonNextPageEnabled = true
            )
        }
    }
}

data class AddAnimalState(
    val isLoading: Boolean = false,
    val isButtonNextPageEnabled: Boolean = false,
    val name: String = "",
    val description: String = "",
    val latLng: LatLng? = null,
    val imageUri: Uri? = null,
    val imageStream: InputStream? = null,
    val currentStateFlow: AddAnimalCurrentStateFlow = AddAnimalCurrentStateFlow.Name
)

sealed class AddAnimalEvent : Event {
    class ShowToast(val message: String) : AddAnimalEvent()
    object OpenPhotoPicker : AddAnimalEvent()
    class ShowNextPage(val actionId: Int) : AddAnimalEvent()
    object FinishFlow : AddAnimalEvent()
    object GoToAuthGraph: AddAnimalEvent()
}

sealed class AddAnimalCurrentStateFlow {
    object Name : AddAnimalCurrentStateFlow()
    object Photo : AddAnimalCurrentStateFlow()
    object Position : AddAnimalCurrentStateFlow()
}