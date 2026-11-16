package com.common.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Like [kotlinx.coroutines.flow.stateIn], but returns a [MutableStateFlow]:
 * the upstream keeps pushing values into it, and callers may also write to it
 * (e.g. optimistic UI updates that are later overwritten by the source of truth).
 */
fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T> {
    val state = MutableStateFlow(initialValue)
    scope.launch {
        distinctUntilChanged().collect(state)
    }
    return state
}
