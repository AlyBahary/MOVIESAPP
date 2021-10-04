package com.beetleware.ugt.network.models

import com.google.gson.annotations.SerializedName

data class GeneralModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)