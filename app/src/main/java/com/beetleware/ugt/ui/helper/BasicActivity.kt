package com.beetleware.ugt.ui.helper

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.beetleware.ugt.R
import com.beetleware.ugt.utils.AndroidApplication
import com.beetleware.ugt.utils.CommonUtil
import com.beetleware.ugt.utils.Config
import java.util.*

abstract class BasicActivity<DB : ViewBinding> : AppCompatActivity(), BaseView {
    var recivedBundle: Bundle? = null

    lateinit var dataBinding: DB

    private var layout: RelativeLayout? = null

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        changeLang(CommonUtil.currentLang)
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        recivedBundle = intent.extras
        getViewModel()
        initView()
        init(savedInstanceState)


    }


//

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    protected override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(Config.PREFS_NAME, Context.MODE_PRIVATE)
        val language: String = sharedPref.getString(
            "appLanguage",
            "en"
        )!! // Helper method to get saved language from SharedPreferences
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }

    //get last state
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        AndroidApplication.setAppLocale(
            CommonUtil.commonSharedPref().getString("appLanguage", "en")
        )
    }

    private fun initView() {
        if (layout == null) {
            layout = RelativeLayout(this)
            layout!!.setClickable(true)
            val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
            progressBar.isIndeterminate = true
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(100, 100)
            val params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layout!!.addView(progressBar, params)
            this.addContentView(layout, params1)
            layout!!.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.transparentDarkBlue
                )
            )
            layout!!.setVisibility(View.GONE)
        }
    }

    abstract fun getViewModel()

    fun isEmptyEditText(editText: EditText): Boolean {
        return if (editText.text.toString().trim().isEmpty()) {
            editText.error = getString(R.string.this_field_is_required)
            editText.requestFocus()
            true
        } else
            false
    }

    fun addProgressBar(): RelativeLayout? {
        layout!!.setVisibility(View.VISIBLE)
        return layout
    }

    fun hideProgressBar() {
        layout!!.setVisibility(View.GONE)
    }

    private fun changeLang(lang:String){

        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

    }

}