@file:Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.bounce.formapp.data

import android.content.Context
import android.net.ConnectivityManager
import com.bounce.formapp.util.BASE_URL
import com.bounce.formapp.util.IS_DEBUG_ON
import com.bounce.formapp.util.error.ErrorInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object APIClient {
    private var retrofit: Retrofit? = null

    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                val builder = OkHttpClient.Builder()

                if (IS_DEBUG_ON) {
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    builder.addNetworkInterceptor(httpLoggingInterceptor)
                }

                builder.addInterceptor(ErrorInterceptor())

                val okClient = builder.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(okClient)
                    .build()
            }

            return retrofit as Retrofit
        }

    val apiInterface: APIInterface
        get() = retrofitInstance.create(
            APIInterface::class.java
        )

    fun isNetworkConnected(context: Context): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}