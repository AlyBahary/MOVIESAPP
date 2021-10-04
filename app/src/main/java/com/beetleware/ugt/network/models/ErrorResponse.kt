package com.beetleware.ugt.network.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("msg")
    val message: String,
//    @SerializedName("errors")
//    val errors: Map<String,ArrayList<String>>
)