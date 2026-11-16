package com.common.coroutines

import com.common.ApiError
import com.common.Result
import kotlinx.coroutines.CancellationException
import java.io.IOException

/**
 * Runs [action] and wraps its outcome into a [Result].
 *
 * - network failures ([IOException]) become [ApiError.NoInternetError];
 * - any other exception becomes [ApiError.OtherError];
 * - [CancellationException] is **rethrown**, so structured concurrency keeps working
 *   (swallowing it would keep a cancelled coroutine running).
 */
suspend fun <T> safeResultApiCall(action: suspend () -> T): Result<T, ApiError> =
    try {
        Result.Success(action())
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Result.Error(ApiError.NoInternetError)
    } catch (e: Exception) {
        Result.Error(ApiError.OtherError)
    }
