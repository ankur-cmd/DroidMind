package com.droidmind.feature.chat.presentation

import com.droidmind.feature.chat.domain.model.Message

/**
 * Represents everything the chat screen needs to render at any given
 * moment — the conversation so far, whether a response is being awaited,
 * and any error message to show.
 *
 * This is a presentation-layer type, distinct from the domain [Message]
 * list it wraps — it exists purely to describe *what the UI should show*,
 * which may need fields (like [isLoading]) that have no meaning in the
 * domain layer.
 *
 * @param messages the conversation history to display, oldest first.
 * @param isLoading true while waiting for the AI's response.
 * @param errorMessage a user-facing error description, or null if none.
 */
data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)