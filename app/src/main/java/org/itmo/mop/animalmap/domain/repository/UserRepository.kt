package org.itmo.mop.animalmap.domain.repository

interface UserRepository {

    suspend fun checkIfUserAlreadyExists(phone: String): Boolean

    suspend fun checkIfLoginAlreadyExists(login: String): Boolean

    suspend fun authenticateUser(phone: String, otpCode: String, login: String): Boolean
}