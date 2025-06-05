package com.example.scannerapp.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.scannerapp.R
import com.example.scannerapp.core.App

abstract class BaseActivity<VM:BaseViewModel>(private val viewmodelClass:Class<VM>): AppCompatActivity() {
    lateinit var viewModel: VM

    open fun viewModel(){}

    abstract fun getLayout():Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        viewModel= ViewModelProvider(this)[viewmodelClass]
        init()
    }


    abstract fun init()
}