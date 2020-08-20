package com.bounce.formapp.data

import com.bounce.formapp.data.response.APIActionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Part

interface APIInterface {

    @FormUrlEncoded
    @POST("/Form_submit.php")
    fun formSubmit(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part("email") email: RequestBody,
        @Part cv: MultipartBody.Part?
    ): Call<APIActionResponse>
}