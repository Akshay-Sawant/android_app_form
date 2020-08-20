package com.bounce.formapp.util.error

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.bounce.formapp.data.APIClient
import com.bounce.formapp.data.response.APIErrorResponse
import com.bounce.formapp.util.IS_DEBUG_ON
import com.bounce.formapp.util.server.Server400ResponseException
import com.bounce.formapp.util.server.Server401ResponseException
import com.bounce.formapp.util.server.ServerInvalidResponseException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.IOException

object ErrorUtils {
    internal fun parseError(response: ResponseBody): APIErrorResponse {
        val converter = APIClient.retrofitInstance
            .responseBodyConverter<APIErrorResponse>(APIErrorResponse::class.java, arrayOfNulls(0))

        val apiErrorResponse: APIErrorResponse
        try {
            apiErrorResponse = converter.convert(response)!!
        } catch (e: IOException) {
            return APIErrorResponse()
        }

        return apiErrorResponse
    }

    fun parseOnFailureException(
        context: Context,
        call: Call<*>,
        t: Throwable
    ) {
        if (t is Server400ResponseException) {
            val apiErrorResponse = t.apiErrorResponse as APIErrorResponse

        } else if (t is Server401ResponseException) {
            if (context is Activity) {
//                SharedPreferenceUtils.startLoginFlow(context)
            } else {
                Toast.makeText(context, "Restart the app to continue", Toast.LENGTH_SHORT).show()
            }
        } else if (t is ServerInvalidResponseException) {
            if (IS_DEBUG_ON) {
                t.printStackTrace()
            }
        } else if (t is JsonEncodingException) {
            if (IS_DEBUG_ON) {
                t.printStackTrace()
            }
            ErrorUtils.logNetworkError(
                "JSONMismatch " + t.message +
                        "\n\nRequest: " + call.request().toString(), t
            )
        } else if (t is JsonDataException) {
            if (IS_DEBUG_ON) {
                t.printStackTrace()
            }

            ErrorUtils.logNetworkError(
                "JSONMismatch " + t.message +
                        "\n\nRequest: " + call.request().toString(), t
            )
        } else if (t is IOException) {
        } else {
            if (IS_DEBUG_ON) {
                t.printStackTrace()
            }

            ErrorUtils.logNetworkError(
                "UnhandledError \n\nRequest: " + call.request().toString(),
                t
            )
        }
    }

    fun logNetworkError(message: String, e: Throwable?) {

    }
}