package com.bounce.formapp.data.response

import com.squareup.moshi.Json

class APIActionResponse {

    @field:Json(name = "status")
    val isActionSuccess: Boolean = false

    @field:Json(name = "msg")
    val mMessage: String =
        "We are unable to process the request at the moment, please try again after a while"
}