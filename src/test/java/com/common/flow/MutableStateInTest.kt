package com.common.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MutableStateInTest {

    @Test
    fun `mirrors upstream and allows local writes`() = runTest {
        val upstream = MutableSharedFlow<Int>()
        val scope = backgroundScope + UnconfinedTestDispatcher(testScheduler)
        val state = upstream.mutableStateIn(scope, initialValue = 0)

        assertEquals(0, state.value)

        upstream.emit(1)
        assertEquals(1, state.value)

        state.value = 10 // local optimistic update
        assertEquals(10, state.value)

        upstream.emit(2)
        assertEquals(2, state.value)
    }
}
