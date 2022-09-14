package org.itmo.mop.animalmap.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.itmo.mop.animalmap.data.NetworkConfig
import org.itmo.mop.animalmap.data.api.CoordinatesAPI
import org.itmo.mop.animalmap.data.repository.CoordinatesRepositoryImpl
import org.itmo.mop.animalmap.domain.repository.CoordinatesRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [RepositoryModule::class])
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addClient()
        .addJsonConverter()
        .build()

    @Singleton
    @Provides
    fun provideRecipeApi(retrofit: Retrofit): CoordinatesAPI =
        retrofit.create(CoordinatesAPI::class.java)

}

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindRecipeRepository(planetaryRepository: CoordinatesRepositoryImpl): CoordinatesRepository
}


private fun Retrofit.Builder.addClient() = apply {
    client(
        OkHttpClient.Builder()
            .addNetworkInterceptor(getHttpLoggingInterceptor())
            .build()
    )
}

@ExperimentalSerializationApi
private fun Retrofit.Builder.addJsonConverter(): Retrofit.Builder = apply {
    val json = Json { ignoreUnknownKeys = true }
    val contentType = "application/json".toMediaType()
    addConverterFactory(json.asConverterFactory(contentType))
}

private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}