package org.itmo.mop.animalmap.domain.repository

import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import java.io.InputStream

interface CoordinatesRepository {

    suspend fun getAllCoordinates(): List<AnimalCoordinate>

    suspend fun getAnimalInfoBy(animalId: Int): AnimalCoordinate

    suspend fun addAnimal(
        name: String,
        description: String,
        latitude: Double,
        longitude: Double,
        photo: InputStream?
    ): Int
}