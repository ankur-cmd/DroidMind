package com.droidmind.feature.chat.data.di

import com.droidmind.feature.chat.data.repository.ChatRepositoryImpl
import com.droidmind.feature.chat.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that binds the [ChatRepository] interface to its concrete
 * implementation, [ChatRepositoryImpl].
 *
 * This is the exact point where Dependency Inversion becomes real at
 * runtime: anywhere in the app that requests a [ChatRepository] (e.g.
 * [com.droidmind.feature.chat.domain.usecase.SendMessageUseCase]) will
 * receive a [ChatRepositoryImpl] instance, without that requesting code
 * ever needing to know the concrete type.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds [ChatRepositoryImpl] as the implementation to provide whenever
     * a [ChatRepository] is requested.
     *
     * @param impl the concrete implementation Hilt should supply.
     */
    @Binds
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository
}