package com.imdb.movies.network.models

import com.google.gson.annotations.SerializedName

data class GeneralResponse<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("error")
    val error: ErrorResponse,
    @SerializedName("msg")
    val msg: String,

    // for Booking Only
    @SerializedName("reservation_id")
    val reservation_id: Int
)