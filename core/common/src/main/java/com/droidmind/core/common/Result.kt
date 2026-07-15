package com.droidmind.core.common

/**
 * A generic wrapper representing the outcome of an operation that can either
 * succeed with a value of type [T], or fail with a typed [AppError].
 *
 * This is used throughout the domain and data layers instead of throwing
 * exceptions directly, so that callers (use cases, ViewModels) can handle
 * success and failure explicitly and exhaustively via a `when` expression.
 *
 * @param T the type of the value produced on success.
 */
sealed class Result<out T> {
    /**
     * Represents a successful outcome.
     *
     * @param data the successfully produced value.
     */

    data class Success<out T>(val data: T) : Result<T>()
    /**
     * Represents a failed outcome.
     *
     * @param exception the typed error describing what went wrong.
     */
    data class Error(val exception: AppError) : Result<Nothing>()
}

/**
 * A closed (sealed) set of error categories that can occur across DroidMind's
 * data and domain layers — for example, when calling an AI provider's API.
 *
 * Modeling errors as a sealed class (rather than passing around a raw
 * [Throwable]) lets calling code react differently to different failure
 * types (e.g. showing "check your connection" for a [NetworkError] vs.
 * "check your API key" for a 401 [ApiError]), and lets the compiler enforce
 * that every error case is handled.
 */
sealed class AppError {
    /**
     * The request never reached the server — e.g. no internet connection,
     * DNS failure, or a connection timeout.
     *
     * @param message a human-readable description of what went wrong.
     */
    data class NetworkError(val message: String) : AppError()

    /**
     * The server received the request but responded with an error status.
     *
     * @param code the HTTP status code returned by the server (e.g. 401, 429, 500).
     * @param message the error message returned by the server, or a fallback description.
     */
    data class ApiError(val code: Int, val message: String) : AppError()

    /**
     * A catch-all for errors that don't fit the categories above — an escape
     * hatch so unexpected failures still have somewhere to go instead of
     * crashing the error model itself.
     *
     * @param throwable the original exception, if available, kept for logging/debugging.
     */
    data class UnknownError(val throwable: Throwable? = null) : AppError()
}

/**
 * Transforms this [Result] into a single value of type [R] by applying
 * exactly one of the two given functions, depending on whether this is a
 * [Result.Success] or a [Result.Error].
 *
 * This lets calling code (typically a ViewModel) collapse a [Result] into
 * whatever it actually needs — a UI state, a plain value, a log message —
 * in one expression, without manually writing a `when` block every time.
 *
 * @param onSuccess called with the wrapped value if this is a [Result.Success].
 * @param onError called with the wrapped [AppError] if this is a [Result.Error].
 * @return the value produced by whichever function was invoked.
 */
inline fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onError: (AppError) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(exception)
}