package com.droidmind.feature.chat.domain.repository

import com.droidmind.core.common.Result
import com.droidmind.feature.chat.domain.model.Message

/**
 * Defines the contract for sending chat messages and receiving AI responses.
 *
 * This interface lives in the domain layer and has no knowledge of *how*
 * the message is actually sent (which AI provider, which HTTP client, etc.).
 * The data layer provides a concrete implementation (e.g. `ChatRepositoryImpl`
 * calling OpenAI's API), which gets wired in via dependency injection.
 *
 * Because the domain layer only ever depends on this interface — never on
 * a concrete implementation — the underlying AI provider can be swapped
 * (OpenAI, Gemini, Claude, etc.) without any change to domain or
 * presentation code. This is the Dependency Inversion Principle in practice.
 */
interface ChatRepository {

    /**
     * Sends a user's message to the AI and returns its response.
     *
     * @param message the text the user typed or spoke.
     * @return a [Result] containing the AI's response [Message] on success,
     * or a typed [com.droidmind.core.common.AppError] on failure.
     */
    suspend fun sendMessage(message: String): Result<Message>
}