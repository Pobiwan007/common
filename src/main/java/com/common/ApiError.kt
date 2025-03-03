package com.common

import com.common.R

sealed class ApiError(val message: Int): Error {
    data object NoInternetError: ApiError(message = R.string.no_internet_error)
    data object ServerError: ApiError(message = R.string.server_error)
    data object OtherError: ApiError(message = R.string.other_error)
    data object EmptyError: ApiError(message = R.string.no_matches_found)
}