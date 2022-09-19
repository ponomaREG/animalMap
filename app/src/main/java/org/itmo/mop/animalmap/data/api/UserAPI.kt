package org.itmo.mop.animalmap.data.api

import okhttp3.RequestBody
import org.itmo.mop.animalmap.data.model.AuthTokenResponse
import org.itmo.mop.animalmap.data.model.UserExistenceResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserAPI {

    @Multipart
    @POST("auth/phone")
    suspend fun checkIfUserAlreadyExists(
        @Part("phone") phone: RequestBody,
    ): UserExistenceResponse

    @Multipart
    @POST("auth/login")
    suspend fun checkIfLoginAlreadyExists(
        @Part("login") login: RequestBody,
    ): UserExistenceResponse

    @Multipart
    @POST("auth")
    suspend fun authenticateUser(
        @Part("phone") phone: RequestBody,
        @Part("otpCode") otpCode: RequestBody,
        @Part("login") login: RequestBody? = null,
    ): AuthTokenResponse
}