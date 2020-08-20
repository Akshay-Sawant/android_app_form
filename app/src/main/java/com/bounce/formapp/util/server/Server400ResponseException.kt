package com.bounce.formapp.util.server

import com.bounce.formapp.data.response.APIErrorResponse
import java.io.IOException

class Server400ResponseException internal constructor(val apiErrorResponse: APIErrorResponse?) :
    IOException()
