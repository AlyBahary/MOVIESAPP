package com.imdb.movies.network.repository

import com.imdb.movies.network.services.ApiService
import com.imdb.movies.utils.CommonUtil
import com.imdb.movies.utils.Config
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(
        data: String,
        password: String,
        device_id: String,
        firebaseToken: String
    ) = apiService.login(data, password, device_id, firebaseToken)

}