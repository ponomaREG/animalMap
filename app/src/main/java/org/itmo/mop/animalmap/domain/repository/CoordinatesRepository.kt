package org.itmo.mop.animalmap.domain.repository

import org.itmo.mop.animalmap.domain.model.AnimalCoordinate

interface CoordinatesRepository {

    suspend fun getAllCoordinates(): List<AnimalCoordinate>
}