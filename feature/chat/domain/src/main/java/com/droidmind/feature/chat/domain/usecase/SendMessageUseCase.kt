package com.droidmind.feature.chat.domain.usecase

import com.droidmind.core.common.AppError
import com.droidmind.core.common.Result
import com.droidmind.feature.chat.domain.model.Message
import com.droidmind.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Sends a user's chat message to the AI and returns its response.
 *
 * This use case represents a single, focused business action: "send a
 * message." It depends only on the [ChatRepository] *interface*, never on
 * a concrete implementation, keeping it fully testable with a fake/mock
 * repository and independent of any specific AI provider.
 *
 * @property chatRepository the repository used to actually send the message.
 */
class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    /**
     * Executes this use case.
     *
     * Rejects blank messages before ever reaching the repository, since an
     * empty message is a business rule violation, not a network concern.
     *
     * @param message the text the user typed or spoke.
     * @return a [Result] containing the AI's response [Message] on success,
     * or a typed [AppError] on failure — including validation failures.
     */
    suspend operator fun invoke(message: String): Result<Message> {
        if (message.isBlank()) {
            return Result.Error(AppError.UnknownError())
        }
        return chatRepository.sendMessage(message)
    }
}