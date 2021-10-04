package com.beetleware.ugt.network.repository

import com.beetleware.ugt.network.services.ApiService
import com.beetleware.ugt.utils.CommonUtil
import com.beetleware.ugt.utils.Config
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.Part
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun skipLogin(deviceId: String) = apiService.skipLogin(deviceId)
    suspend fun login(
        data: String,
        password: String,
        device_id: String,
        firebaseToken: String
    ) = apiService.login(data, password, device_id, firebaseToken)

    suspend fun updateFireBaseToken(
    ) = apiService.updateFireBaseToken(CommonUtil.getDataFromSharedPref(Config.FIREBASE_TOKEN))


}