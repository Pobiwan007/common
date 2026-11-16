package com.common.coroutines

import com.common.ApiError
import com.common.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class SafeResultApiCallTest {

    @Test
    fun `wraps returned value into Success`() = runTest {
        assertEquals(Result.Success<Int, ApiError>(42), safeResultApiCall { 42 })
    }

    @Test
    fun `maps IOException to NoInternetError`() = runTest {
        val result = safeResultApiCall<Int> { throw IOException("offline") }
        assertEquals(Result.Error<Int, ApiError>(ApiError.NoInternetError), result)
    }

    @Test
    fun `maps unexpected exceptions to OtherError`() = runTest {
        val result = safeResultApiCall<Int> { error("boom") }
        assertEquals(Result.Error<Int, ApiError>(ApiError.OtherError), result)
    }

    @Test
    fun `rethrows CancellationException`() = runTest {
        try {
            safeResultApiCall<Int> { throw CancellationException("cancelled") }
            fail("CancellationException must not be swallowed")
        } catch (e: CancellationException) {
            assertEquals("cancelled", e.message)
        }
    }
}
