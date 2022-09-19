package org.itmo.mop.animalmap.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.itmo.mop.animalmap.data.persistence.TokenPersistence
import javax.inject.Inject

class AuthHeaderInterceptor @Inject constructor(
    private val tokenPersistence: TokenPersistence,
): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request().newBuilder().apply {
                if(tokenPersistence.isTokenExists()) {
                    val token = tokenPersistence.getToken()!!
                    addHeader("token", token)
                }
            }.build()
        )
    }
}