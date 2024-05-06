package com.pep.pod.data.utils.network

import com.pep.pod.data.dto.PodResponse
import com.pep.pod.data.dto.ErrorResponse
import okhttp3.Headers
import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * A request that resulted in a response with a 2xx status code that has a body.
     */
    data class Success<T : Any>(val body: T, val headers: Headers? = null, val code: Int) : NetworkResponse<T, Nothing>()

    /**
     * A request that resulted in a response with a non-2xx status code.
     */
    data class ServerError<U : Any>(val body: U?, val code: Int, val headers: Headers? = null) : NetworkResponse<Nothing, U>()

    /**
     * A request that didn't result in a response.
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * A request that resulted in an error different from an IO or Server error.
     *
     * An example of such an error is JSON parsing exception thrown by a serialization library.
     */
    data class UnknownError(val error: Throwable, val code: Int? = null, val headers: Headers? = null, ) : NetworkResponse<Nothing, Nothing>()
}

fun <T : Any> podResponseMapper(networkResponse: NetworkResponse<PodResponse<T>, ErrorResponse>): T =
     when (networkResponse) {
        is NetworkResponse.Success<PodResponse<T>> ->
            networkResponse.body.result
        is NetworkResponse.ServerError<ErrorResponse> ->
            throw Throwable(message = networkResponse.body?.message?.get(0) ?: "Server error connection")
        is NetworkResponse.NetworkError ->
            throw  Throwable("Please check your connection")
        is NetworkResponse.UnknownError ->
            throw  Throwable("Something went wrong")
    }

