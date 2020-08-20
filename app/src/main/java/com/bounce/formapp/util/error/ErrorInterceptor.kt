package com.bounce.formapp.util.error

import com.bounce.formapp.data.response.APIErrorResponse
import com.bounce.formapp.util.server.Server400ResponseException
import com.bounce.formapp.util.server.Server401ResponseException
import com.bounce.formapp.util.server.ServerInvalidResponseException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import okio.BufferedSource

import java.io.IOException

internal class ErrorInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)

        if (response.isSuccessful) {
            return response
        } else if (response.code == 500) {
            val e =
                ServerInvalidResponseException(
                    ServerInvalidResponseException.ERROR_500_RESPONSE
                )
            ErrorUtils.logNetworkError(
                ServerInvalidResponseException.ERROR_500_RESPONSE +
                        " \nRequest: " + request.toString() +
                        "\nResponse: " + response.toString(), e
            )
            throw e
        } else if (response.code == 401) {
            throw Server401ResponseException()

        } else if (response.code == 400) {
            val responseBody = response.body
            if (responseBody != null) {
                val source: BufferedSource = responseBody.source()
                source.request(Long.MAX_VALUE)
                val bufferCopy: Buffer = source.buffer.clone()
                val responseBodyCopy = bufferCopy.asResponseBody(
                    responseBody.contentType(),
                    bufferCopy.size
                )
                val apiErrorResponse: APIErrorResponse =
                    ErrorUtils.parseError(
                        responseBodyCopy
                    )
                throw Server400ResponseException(
                    apiErrorResponse
                )
            } else {
                val e =
                    ServerInvalidResponseException(
                        ServerInvalidResponseException.ERROR_400_BLANK_RESPONSE
                    )
                ErrorUtils.logNetworkError(
                    ServerInvalidResponseException.ERROR_400_BLANK_RESPONSE +
                            " \nRequest: " + request.toString() +
                            "\nResponse: " + response.toString(), e
                )
                throw e
            }
        } else {
            val e =
                ServerInvalidResponseException(
                    ServerInvalidResponseException.ERROR_UNEXPECTED_STATUS_CODE
                )
            ErrorUtils.logNetworkError(
                ServerInvalidResponseException.ERROR_UNEXPECTED_STATUS_CODE +
                        " \nRequest: " + request.toString() +
                        "\nResponse: " + response.toString(), e
            )
            throw e
        }
    }
}
