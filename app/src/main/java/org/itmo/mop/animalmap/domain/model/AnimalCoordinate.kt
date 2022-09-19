package org.itmo.mop.animalmap.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimalCoordinate(
    val id: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val description: String,
    val image: String?,
    val creatorLogin: String?,
) : Parcelable