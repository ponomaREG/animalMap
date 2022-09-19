package org.itmo.mop.animalmap.data.repository

import okhttp3.RequestBody.Companion.toRequestBody
import org.itmo.mop.animalmap.data.api.UserAPI
import org.itmo.mop.animalmap.data.model.ExistenceStatus
import org.itmo.mop.animalmap.data.persistence.TokenPersistence
import org.itmo.mop.animalmap.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userAPI: UserAPI,
    private val tokenPersistence: TokenPersistence,
): UserRepository {

    override suspend fun checkIfUserAlreadyExists(phone: String): Boolean {
        return userAPI.checkIfUserAlreadyExists(phone.toRequestBody()).status == ExistenceStatus.EXISTS
    }

    override suspend fun checkIfLoginAlreadyExists(login: String): Boolean {
        return userAPI.checkIfLoginAlreadyExists(login.toRequestBody()).status == ExistenceStatus.EXISTS
    }

    override suspend fun authenticateUser(
        phone: String,
        otpCode: String,
        login: String
    ): Boolean {
        val authenticateUser = userAPI.authenticateUser(
            phone = phone.toRequestBody(),
            otpCode = otpCode.toRequestBody(),
            login = login.takeIf { it.isNotEmpty() }?.toRequestBody()
        )
        tokenPersistence.saveTokenImmediately(authenticateUser.token)
        return true //TODO: Добавить проверки на успешную аутентификацию
    }
}