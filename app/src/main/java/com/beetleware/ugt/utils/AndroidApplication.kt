package com.beetleware.ugt.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class AndroidApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        if (!Hawk.isBuilt())
            Hawk.init(appContext).build();

    }

    fun checkIfHasNetwork(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    companion object {
        //key path  : /Applications/Android Studio.app/Contents/bin/key
        var appContext: Context? = null
            private set
        var instance: AndroidApplication? = null
            private set

        fun hasNetwork(): Boolean {
            return instance!!.checkIfHasNetwork()
        }

        fun setAppLocale(lang: String?): Boolean {
            if (lang != null) {
                val sharedPreferences =
                    appContext!!.getSharedPreferences(Config.PREFS_NAME, MODE_PRIVATE)
                val sharedPrefEditor = sharedPreferences.edit()
                sharedPrefEditor.putString("appLanguage", lang)
                sharedPrefEditor.apply()
                sharedPrefEditor.commit()
                val myLocale = Locale(lang)
                val res = appContext!!.resources
                val dm = res.displayMetrics
                val conf = res.configuration
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    conf.setLocale(myLocale)
                } else {
                    conf.locale = myLocale
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    conf.setLayoutDirection(myLocale)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    appContext!!.createConfigurationContext(conf)
                } else {
                    res.updateConfiguration(conf, dm)
                }
                return true
            }
            return false
        }
    }
}