package org.itmo.mop.animalmap.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.itmo.mop.animalmap.data.model.AddAnimalResponse
import org.itmo.mop.animalmap.data.model.AnimalCoordinatesNetwork
import retrofit2.http.*

interface CoordinatesAPI {

    @GET("animal")
    suspend fun getAllCoordinates(): List<AnimalCoordinatesNetwork>

    @GET("animal/{id}")
    suspend fun getAnimalInfo(@Path("id") animalId: Int): AnimalCoordinatesNetwork

    @Multipart
    @POST("animal/add")
    suspend fun addAnimal(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): AddAnimalResponse
}