package com.common

import com.common.R


sealed class UsernameValidationError(val textResource: Int): Error {
    data object ShortUsername: UsernameValidationError(textResource = R.string.short_username)
    data object EmptyUsername: UsernameValidationError(textResource = R.string.empty_username)
    data object LongUsername: UsernameValidationError(textResource = R.string.long_username)
}