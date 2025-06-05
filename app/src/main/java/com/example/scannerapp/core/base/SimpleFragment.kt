package com.example.scannerapp.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/*
    SimpleFragment use for simple fragment
 */

abstract class SimpleFragment:Fragment() {
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        init(inflater, container)
        init()
        return rootView
    }

    abstract fun getLayout(): Int

    private fun init(inflater: LayoutInflater, container: ViewGroup?) {
        rootView = inflater.inflate(getLayout(), container, false)
    }

    abstract fun init()
}