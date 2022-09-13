package org.itmo.mop.animalmap.data.api

import org.itmo.mop.animalmap.data.model.AnimalCoordinatesNetwork

interface CoordinatesAPI {

    suspend fun getAllCoordinates(): List<AnimalCoordinatesNetwork>
}