package com.droidmind.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidmind.core.common.fold
import com.droidmind.feature.chat.domain.model.Message
import com.droidmind.feature.chat.domain.model.Sender
import com.droidmind.feature.chat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the chat screen.
 *
 * Holds and updates [ChatUiState] in response to user actions (sending a
 * message), delegating the actual business logic to [SendMessageUseCase]
 * rather than talking to any repository or network code directly.
 *
 * @property sendMessageUseCase the use case invoked when the user sends a message.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())

    /** The current UI state, observed by [ChatScreen]. */
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    /**
     * Called when the user sends a message. Adds the user's message to the
     * conversation immediately, then invokes [sendMessageUseCase] and
     * updates the state with either the AI's reply or an error.
     *
     * @param text the message text the user typed.
     */
    fun onSendMessage(text: String) {
        val userMessage = Message(content = text, sender = Sender.USER)

        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage,
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = sendMessageUseCase(text)

            result.fold(
                onSuccess = { aiMessage ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + aiMessage,
                            isLoading = false
                        )
                    }
                },
                onError = { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = error.toString()
                        )
                    }
                }
            )
        }
    }
}