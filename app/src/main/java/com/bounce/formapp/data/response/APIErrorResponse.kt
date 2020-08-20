package com.bounce.formapp.data.response

import com.squareup.moshi.Json

class APIErrorResponse {

    @field:Json(name = "title")
    val title: String = "Server busy!"

    @field:Json(name = "message")
    val message: String = "We are unable to process the request at the moment, please try again after a while"
}
