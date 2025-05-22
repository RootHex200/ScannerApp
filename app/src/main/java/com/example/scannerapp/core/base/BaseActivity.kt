package com.example.scannerapp.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.scannerapp.R

abstract class BaseActivity<VM:BaseViewModel>(private val viewmodelClass:Class<VM>):SimpleActivity() {
    lateinit var viewModel: VM

    open fun viewModel(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        viewModel= ViewModelProvider(this)[viewmodelClass]
        viewModel()
        init()
    }
}