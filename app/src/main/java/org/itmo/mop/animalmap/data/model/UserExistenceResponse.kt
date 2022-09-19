package org.itmo.mop.animalmap.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserExistenceResponse(
    @SerialName("status") val status: ExistenceStatus
)

@Serializable
enum class ExistenceStatus(val status: String) {

    @SerialName("Exists")
    EXISTS("Exists"),

    @SerialName("Not exists")
    NOT_EXISTS("Not exists")
}
