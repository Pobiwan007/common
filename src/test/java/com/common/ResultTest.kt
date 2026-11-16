package com.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    private val success: Result<Int, ApiError> = Result.Success(2)
    private val error: Result<Int, ApiError> = Result.Error(ApiError.ServerError)
    private val loading: Result<Int, ApiError> = Result.Loading

    @Test
    fun `map transforms success data`() {
        assertEquals(Result.Success<String, ApiError>("4"), success.map { (it * 2).toString() })
    }

    @Test
    fun `map keeps error and loading untouched`() {
        assertEquals(Result.Error<String, ApiError>(ApiError.ServerError), error.map { it.toString() })
        assertSame(Result.Loading, loading.map { it + 1 })
    }

    @Test
    fun `onSuccess and onError run only for the matching branch`() {
        val calls = mutableListOf<String>()

        success.onSuccess { calls += "success:$it" }.onError { calls += "error:$it" }
        error.onSuccess { calls += "success:$it" }.onError { calls += "error:$it" }

        assertEquals(listOf("success:2", "error:ServerError"), calls)
    }

    @Test
    fun `dataOrNull returns data only for success`() {
        assertEquals(2, success.dataOrNull())
        assertNull(error.dataOrNull())
        assertNull(loading.dataOrNull())
    }

    @Test
    fun `api errors expose a string resource`() {
        val errors = listOf(
            ApiError.NoInternetError,
            ApiError.ServerError,
            ApiError.OtherError,
            ApiError.EmptyError
        )
        assertTrue(errors.all { it.message != 0 })
        assertEquals(errors.size, errors.map { it.message }.toSet().size)
    }
}
