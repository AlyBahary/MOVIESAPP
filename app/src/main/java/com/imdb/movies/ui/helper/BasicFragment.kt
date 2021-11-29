package com.imdb.movies.ui.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BasicFragment<DB : ViewBinding> : Fragment(),BaseView {
    lateinit var dataBinding: DB

    private fun initDataBinding(inflater: LayoutInflater, container: ViewGroup) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), null, false);
//        dataBinding =DataBindingUtil.setContentView(requireActivity(), getLayoutRes())

//        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        container?.let {
            initDataBinding(inflater, it)
        }
        return dataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }




}