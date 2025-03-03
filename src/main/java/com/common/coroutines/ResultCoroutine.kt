package com.common.coroutines

import com.common.ApiError
import com.common.Result

suspend fun <T> safeResultApiCall(action: suspend () -> T): Result<T, ApiError> {
    return try {
        Result.Success(action())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(error = ApiError.OtherError)
    }
}