package com.imdb.movies.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imdb.movies.R
import com.imdb.movies.databinding.ActivitySplashBinding
import com.imdb.movies.ui.helper.BasicActivity

class SplashActivity : BasicActivity<ActivitySplashBinding>() {

    override fun getViewModel() {
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun getLayoutRes(): Int = R.layout.activity_splash
}