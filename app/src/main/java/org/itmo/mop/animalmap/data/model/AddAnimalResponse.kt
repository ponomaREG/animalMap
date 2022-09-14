package org.itmo.mop.animalmap.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddAnimalResponse(
    @SerialName("newAnimalId") val newAnimalId: Int,
)
