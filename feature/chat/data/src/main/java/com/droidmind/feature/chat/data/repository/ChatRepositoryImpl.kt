package com.droidmind.feature.chat.data.repository

import android.util.Log
import com.droidmind.core.common.Result
import com.droidmind.core.common.AppError
import com.droidmind.feature.chat.data.BuildConfig
import com.droidmind.feature.chat.data.remote.GeminiApiService
import com.droidmind.feature.chat.data.remote.dto.GeminiContent
import com.droidmind.feature.chat.data.remote.dto.GeminiPart
import com.droidmind.feature.chat.data.remote.dto.GeminiRequest
import com.droidmind.feature.chat.domain.model.Message
import com.droidmind.feature.chat.domain.model.Sender
import com.droidmind.feature.chat.domain.repository.ChatRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of [ChatRepository] that sends chat messages to Google's
 * Gemini API via [GeminiApiService].
 *
 * This class lives entirely in the data layer — the domain layer only ever
 * references the [ChatRepository] interface, never this class directly.
 * Hilt is responsible for supplying this implementation wherever a
 * [ChatRepository] is requested (wired via a `@Binds` declaration).
 *
 * @property geminiApiService the Retrofit service used to call Gemini.
 */
class ChatRepositoryImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
) : ChatRepository {

    private companion object {
        const val GEMINI_MODEL = "gemini-flash-latest"
    }

    override suspend fun sendMessage(message: String): Result<Message> {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(parts = listOf(GeminiPart(text = message)))
                )
            )

            val response = geminiApiService.generateContent(
                model = GEMINI_MODEL,
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )

            val replyText = response.candidates
                .firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

            if (replyText != null) {
                Result.Success(
                    Message(content = replyText, sender = Sender.AI)
                )
            } else {
                Result.Error(
                    AppError.UnknownError()
                )
            }
        } catch (e: IOException) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error occurred"))
        } catch (e: HttpException) {
            Log.e("Gemini", e.response()?.errorBody()?.string() ?: "No error body")
            Result.Error(
                AppError.ApiError(
                    code = e.code(),
                    message = e.message() ?: "API error occurred"
                )
            )
        } catch (e: Exception) {
            Result.Error(AppError.UnknownError(e))
        }
    }
}