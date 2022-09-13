package org.itmo.mop.animalmap.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate

@Serializable
data class AnimalCoordinatesNetwork(
    @SerialName("latitude") val latitude: String,
    @SerialName("longitude") val longitude: String,
)

fun AnimalCoordinatesNetwork.toAnimalCoordinate(): AnimalCoordinate = AnimalCoordinate(
    latitude,
    longitude
)
