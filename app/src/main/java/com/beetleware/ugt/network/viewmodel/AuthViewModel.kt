package com.beetleware.ugt.network.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beetleware.ugt.R
import com.beetleware.ugt.network.models.*
import com.beetleware.ugt.network.repository.Repository
import com.beetleware.ugt.utils.AndroidApplication
import com.beetleware.ugt.network.models.GeneralModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import java.io.Reader
import javax.inject.Inject


@HiltViewModel
public class AuthViewModel @Inject constructor(private val mainRepository: Repository) :
    ViewModel() {

    private var TAG = "MainViewModel"
    val errorMessage = MutableLiveData<String>()
    val skipLoginMutableLiveData = MutableLiveData<JsonObject>()
    val loginMutableLiveData = MutableLiveData<JsonObject>()

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(TAG, ": ${throwable.localizedMessage}")
        onError()
    }
    val loading = MutableLiveData<Boolean>()

    fun skipLogin(deviceId: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.skipLogin(deviceId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    skipLoginMutableLiveData.postValue(response.body())
                    loading.value = false
                } else {

                    onError(response.errorBody()!!.charStream())
                }
            }
        }
    }



    private fun onError(errorBody: Reader) {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        var errorResponse: ErrorResponse? = gson.fromJson(errorBody, type)
        errorMessage.value = errorResponse!!.message
        loading.value = false
    }

    private fun onError() {
        errorMessage.postValue(AndroidApplication.appContext!!.getString(R.string.msg_error_connection))
//        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}