package org.itmo.mop.animalmap.data.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.itmo.mop.animalmap.data.api.CoordinatesAPI
import org.itmo.mop.animalmap.data.model.AnimalCoordinatesNetwork
import org.itmo.mop.animalmap.data.model.toAnimalCoordinate
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import java.io.InputStream
import javax.inject.Inject


class CoordinatesRepositoryImpl @Inject constructor(private val coordinatesAPI: CoordinatesAPI) :
    CoordinatesRepository {

    override suspend fun getAllCoordinates(): List<AnimalCoordinate> {
        return coordinatesAPI.getAllCoordinates().map(AnimalCoordinatesNetwork::toAnimalCoordinate)
    }

    override suspend fun getAnimalInfoBy(animalId: Int): AnimalCoordinate {
        return coordinatesAPI.getAnimalInfo(animalId).toAnimalCoordinate()
    }

    override suspend fun addAnimal(
        name: String,
        description: String,
        latitude: Double,
        longitude: Double,
        photo: InputStream?
    ): Int {
        return if (photo != null) {
            val requestFile: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), photo.readBytes())
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", "image", requestFile)
            coordinatesAPI.addAnimal(
                name = name.toRequestBody(),
                description = description.toRequestBody(),
                latitude = latitude.toString().toRequestBody(),
                longitude = longitude.toString().toRequestBody(),
                image = body

            ).newAnimalId
        } else {
            coordinatesAPI.addAnimal(
                name = name.toRequestBody(),
                description = description.toRequestBody(),
                latitude = latitude.toString().toRequestBody(),
                longitude = longitude.toString().toRequestBody()
            ).newAnimalId
        }
    }

    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

}