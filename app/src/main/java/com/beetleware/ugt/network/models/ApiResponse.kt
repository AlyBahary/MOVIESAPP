package com.beetleware.ugt.network.models

import okhttp3.Headers
import okhttp3.ResponseBody

/**
 * Common class used by API responses.
 * @param <T> the type of the responseBody
 **/

class ApiResponse<T> {

    // if request throws exception and can't be send
    var exception: Throwable? = null
    var isCanceled: Boolean = false

    // if request success and receive a response
    var isResponseSuccessful: Boolean = false // state of response
    var responseCode: Int? = 0 // response code
    var responseHeader: Headers? = null // response headers

    // response body in case of response succeeded (T: the POJO class of the response)
    var responseBody: T? = null

    // response body in case of response failed
    var errorBody: ResponseBody? = null
}