package com.beetleware.ugt.network.di

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.multidex.BuildConfig
import com.beetleware.ugt.network.services.ApiService
import com.beetleware.ugt.utils.AndroidApplication
import com.beetleware.ugt.utils.CommonUtil
import com.beetleware.ugt.utils.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    var REST_API_URL: String = Config.BaseUrl

    @Provides
    @Singleton
    fun apiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(REST_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(provideOkHttpClient())
            .build().create(ApiService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideSentLanguageInterceptor()!!)
            .addInterceptor(provideLogInterceptor())
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                chain.proceed(request)
            })
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .callTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()
    }


    fun provideSentLanguageInterceptor(): Interceptor? {
        return Interceptor { chain: Interceptor.Chain ->

            val request =
                chain.request().newBuilder().addHeader("Content-Type", "application/json")
                    .addHeader(
                        "Authorization",
                        CommonUtil.getDataFromSharedPref(Config.ACCESS_TOKEN)
                    ).addHeader(
                        "lang",
                        CommonUtil.getDataFromSharedPref(Config.APPLANGUAGE)
                    )
                    .build()
            chain.proceed(request)
        }

    }


    fun provideLogInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val originalRequest = chain.request() //Current Request
            var response = chain.proceed(originalRequest) //Get response of the request
            if (response.code() == 401) {
                val am: ActivityManager = AndroidApplication.appContext!!
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val cn: ComponentName = am.getRunningTasks(1).get(0).topActivity!!
                Log.d("TAG", "provideLogInterceptor: " + cn.getClassName())
                if (cn.getClassName().contains("MainActivity")) {
//                    AndroidApplication.appContext!!.startActivity(
//                        Intent(AndroidApplication.appContext!!, SplashActivity::class.java)
//                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    )
                }
            }

            /** DEBUG STUFF  */
            /** DEBUG STUFF  */
            if (BuildConfig.DEBUG) {

//            I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body

                val bodyString = response.body()!!.string()
                Timber.d(
                    String.format(
                        "Sending request %s \n\n with body %s \n\n with headers %s ",
                        originalRequest.url(),
                        if (originalRequest.body() != null) bodyToString(originalRequest) else originalRequest.body(),
                        originalRequest.headers()
                    )
                )
                Timber.d(
                    String.format(
                        "Got response HTTP %s %s \n\n with body %s \n\n with headers %s ",
                        response.code(),
                        response.message(),
                        bodyString,
                        response.headers()
                    )
                )
                response = response.newBuilder()
                    .body(ResponseBody.create(response.body()!!.contentType(), bodyString)).build()


            }
            response
        }
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}