package org.itmo.mop.animalmap.data.repository

import org.itmo.mop.animalmap.data.api.CoordinatesAPI
import org.itmo.mop.animalmap.data.model.AnimalCoordinatesNetwork
import org.itmo.mop.animalmap.data.model.toAnimalCoordinate
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import javax.inject.Inject

class CoordinatesRepositoryImpl @Inject constructor(private val coordinatesAPI: CoordinatesAPI): CoordinatesRepository {

    override suspend fun getAllCoordinates(): List<AnimalCoordinate> {
        return coordinatesAPI.getAllCoordinates().map(AnimalCoordinatesNetwork::toAnimalCoordinate)
    }

}