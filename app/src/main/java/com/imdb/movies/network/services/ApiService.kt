package com.imdb.movies.network.services

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("data") data: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String,
        @Field("firebase_token") firebaseToken: String
    ): Response<JsonObject>




}