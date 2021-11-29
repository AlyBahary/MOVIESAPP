package com.imdb.movies.network.models

import com.google.gson.annotations.SerializedName

data class GeneralModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)