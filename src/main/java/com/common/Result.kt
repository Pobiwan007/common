package com.common

typealias RootError = Error

/**
 * A typed result of an operation: either [Success] with data, [Error] with a domain error,
 * or [Loading] while the operation is in progress.
 *
 * Unlike [kotlin.Result], the error type is part of the signature, so the compiler forces
 * callers to handle every domain error explicitly (`when` over a sealed hierarchy).
 */
sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
    data object Loading : Result<Nothing, Nothing>
}

/** Transforms the data of a [Result.Success]; errors and loading pass through unchanged. */
inline fun <D, E : RootError, R> Result<D, E>.map(transform: (D) -> R): Result<R, E> =
    when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(error)
        Result.Loading -> Result.Loading
    }

/** Runs [action] if this is a [Result.Success] and returns the receiver for chaining. */
inline fun <D, E : RootError> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> =
    apply { if (this is Result.Success) action(data) }

/** Runs [action] if this is a [Result.Error] and returns the receiver for chaining. */
inline fun <D, E : RootError> Result<D, E>.onError(action: (E) -> Unit): Result<D, E> =
    apply { if (this is Result.Error) action(error) }

/** Returns the data of a [Result.Success], or `null` otherwise. */
fun <D, E : RootError> Result<D, E>.dataOrNull(): D? = (this as? Result.Success)?.data
