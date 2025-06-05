package com.example.scannerapp.view.landing.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.scannerapp.R
import com.example.scannerapp.R.color.secondaryColor2
import com.example.scannerapp.core.base.BaseFragment
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.core.base.SimpleFragment
import com.example.scannerapp.view.landing.history.createHistory.createHistory
import com.example.scannerapp.view.landing.history.scanHistory.ScanHistory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRhistory : SimpleFragment() {
    

    override fun getLayout(): Int {
        return R.layout.fragment_q_rhistory
    }

    override fun init() {
        parentFragmentManager.beginTransaction().replace(
            R.id.qrHistoryframlayout,ScanHistory()
        ).commit()


        val scanHistory=rootView.findViewById<LinearLayout>(R.id.scanHistoryBtn)
        val createHistory=rootView.findViewById<LinearLayout>(R.id.createHistoryBtn)

        scanHistory.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.qrHistoryframlayout,ScanHistory()
            ).commit()
            scanHistory.setBackgroundResource(R.drawable.linear_color)
            createHistory.setBackgroundColor(ContextCompat.getColor(rootView.context,R.color.secondaryColor2))
        }
        createHistory.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.qrHistoryframlayout,createHistory()
            ).commit()
            createHistory.setBackgroundResource(R.drawable.linear_color)
            scanHistory.setBackgroundColor(ContextCompat.getColor(rootView.context!!,R.color.secondaryColor2))
        }
    }

}