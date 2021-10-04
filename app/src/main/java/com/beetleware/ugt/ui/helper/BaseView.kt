package com.beetleware.ugt.ui.helper

import android.os.Bundle
import androidx.annotation.LayoutRes

interface BaseView {

    /**
     * use this method to provide the activity layout to be used in dataBinding
     *
     * @return the layout id
     */
    @LayoutRes
    fun getLayoutRes(): Int


    /**
     * Use this method to handle all views in your activities and fragments
     */
    fun init(savedInstanceState: Bundle?)


}
