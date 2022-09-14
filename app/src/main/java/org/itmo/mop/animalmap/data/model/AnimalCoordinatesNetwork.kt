package org.itmo.mop.animalmap.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate

@Serializable
data class AnimalCoordinatesNetwork(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("imageSrc") val image: String?,
    @SerialName("latitude") val latitude: String,
    @SerialName("longitude") val longitude: String,
)

fun AnimalCoordinatesNetwork.toAnimalCoordinate(): AnimalCoordinate = AnimalCoordinate(
    id = id,
    latitude = latitude,
    longitude = longitude,
    name = name,
    description = description,
    image = image
)
