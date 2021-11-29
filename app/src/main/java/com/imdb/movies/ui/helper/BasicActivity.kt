package com.imdb.movies.ui.helper

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.imdb.movies.R
import com.imdb.movies.utils.AndroidApplication
import com.imdb.movies.utils.CommonUtil
import com.imdb.movies.utils.Config
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
        setupLanguage(CommonUtil.currentLang)
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        recivedBundle = intent.extras
        getViewModel()
        initView()
        init(savedInstanceState)


    }



    private fun initView() {
        if (layout == null) {
            layout = RelativeLayout(this)
            layout!!.isClickable = true
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
                    R.color.transparentDarkPrimary
                )
            )
            layout!!.visibility = View.GONE
        }
    }



    fun addProgressBar(): RelativeLayout? {
        layout!!.visibility = View.VISIBLE
        return layout
    }

    fun hideProgressBar() {
        layout!!.visibility = View.GONE
    }

    private fun setupLanguage(lang:String){

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