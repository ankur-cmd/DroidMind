package com.droidmind.feature.chat.data.di

import com.droidmind.feature.chat.data.remote.ApiConstants
import com.droidmind.feature.chat.data.remote.GeminiApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import javax.inject.Singleton

/**
 * Hilt module responsible for providing networking dependencies needed to
 * talk to Google's Gemini API — the [OkHttpClient], [Retrofit] instance,
 * and the [GeminiApiService] itself.
 *
 * Installed in [SingletonComponent], meaning every provided dependency here
 * lives as long as the application process — appropriate since a single
 * shared HTTP client/Retrofit instance should be reused everywhere, rather
 * than recreated per screen.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a configured [OkHttpClient] with request/response logging
     * enabled — invaluable for debugging API calls during development.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provides the [Retrofit] instance configured with Gemini's base URL
     * and a kotlinx. Serialization-based JSON converter.
     *
     * @param okHttpClient the HTTP client this Retrofit instance will use.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(ApiConstants.GEMINI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    /**
     * Provides the [GeminiApiService], created by Retrofit from the
     * interface definition.
     *
     * @param retrofit the configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideGeminiApiService(retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }
}