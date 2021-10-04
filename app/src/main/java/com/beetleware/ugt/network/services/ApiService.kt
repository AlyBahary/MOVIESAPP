package com.beetleware.ugt.network.services

import com.beetleware.ugt.network.models.*
import com.beetleware.ugt.network.models.GeneralModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/skip-login")
    suspend fun skipLogin(@Field("device_id") deviceId: String): Response<JsonObject>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("data") data: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String,
        @Field("firebase_token") firebaseToken: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("auth/update-firebase-token")
    suspend fun updateFireBaseToken(
        @Field("firebase_token") firebaseToken: String
    ): Response<JsonObject>



}