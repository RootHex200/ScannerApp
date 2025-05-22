package com.example.scannerapp.core.base
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

/*
    BaseFragment use for viewmodel fragment
 */

abstract class BaseFragment<VM:BaseViewModel>(private val viewModelClass: Class<VM>) : SimpleFragment() {

    open lateinit var viewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this)[viewModelClass]
        viewModel()
    }

    /*
        viewModel() use for call viewmodel
     */
    open fun viewModel(){}
}
