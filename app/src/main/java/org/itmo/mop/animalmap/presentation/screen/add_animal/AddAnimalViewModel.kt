package org.itmo.mop.animalmap.presentation.screen.add_animal

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import org.itmo.mop.animalmap.presentation.base.BaseViewModel
import org.itmo.mop.animalmap.presentation.base.Event
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddAnimalViewModel @Inject constructor(
    private val coordinatesRepository: CoordinatesRepository,
) : BaseViewModel<AddAnimalState, AddAnimalEvent>() {

    override val initialState: AddAnimalState
        get() = AddAnimalState()

    fun onButtonAddClicked() = with(state.value) {
        viewModelScope.launch {
            val newMarkerId = coordinatesRepository.addAnimal(
                name = name,
                description = description,
                latitude = latLng!!.latitude,
                longitude = latLng.longitude,
                photo = imageStream
            )
            if (newMarkerId < 0) submitEvent(AddAnimalEvent.ShowToast("Не удалось добавить!")) else {
                val newMarker = AnimalCoordinate(
                    id = newMarkerId,
                    name = name,
                    description = description,
                    latitude = latLng.latitude.toString(),
                    longitude = latLng.longitude.toString(),
                    image = null //TODO: С этим желательно что-нибудь сделать
                )
                submitEvent(AddAnimalEvent.CloseActivity(newMarker))
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
                isButtonAddEnabled = name.isNotEmpty() && description.isNotEmpty() && latLng != null
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        updateState {
            copy(
                description = description,
                isButtonAddEnabled = name.isNotEmpty() && description.isNotEmpty() && latLng != null
            )
        }
    }

    fun onCoordinatesChanged(latLng: LatLng) {
        updateState {
            copy(
                latLng = latLng,
                isButtonAddEnabled = name.isNotEmpty() && description.isNotEmpty()
            )
        }
    }

    fun onPhotoPicked(photoUri: Uri, photoStream: InputStream) {
        updateState {
            copy(
                imageUri = photoUri,
                imageStream = photoStream
            )
        }
    }
}

data class AddAnimalState(
    val isLoading: Boolean = false,
    val isButtonAddEnabled: Boolean = false,
    val name: String = "",
    val description: String = "",
    val latLng: LatLng? = null,
    val imageUri: Uri? = null,
    val imageStream: InputStream? = null,
)

sealed class AddAnimalEvent : Event {
    class CloseActivity(val newMarker: AnimalCoordinate) : AddAnimalEvent()
    class ShowToast(val message: String) : AddAnimalEvent()
    object OpenPhotoPicker : AddAnimalEvent()
}