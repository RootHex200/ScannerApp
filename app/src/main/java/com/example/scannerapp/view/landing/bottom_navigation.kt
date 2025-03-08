package com.example.scannerapp.view.landing

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.scannerapp.R
import com.example.scannerapp.view.landing.QrGenerator.QRgenerator
import com.example.scannerapp.view.landing.history.QRhistory
import com.example.scannerapp.view.landing.scanner.QRscanner


class BottomNavigation : AppCompatActivity() {
    private lateinit var frameLayout: FrameLayout
    private lateinit var qrHistory:ImageView
    private lateinit var qrScanner:ImageView
    private lateinit var qrGenerator:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_navigation)

        //default loaded fragment
        loadFragmentView(null)

        //initialize bottomNavigation
        frameLayout=findViewById<FrameLayout>(R.id.fragment_container)
        qrHistory=findViewById<ImageView>(R.id.qrHistory)
        qrScanner=findViewById<ImageView>(R.id.qrScanner)
        qrGenerator=findViewById<ImageView>(R.id.qrGenerator)

        qrScanner.setOnClickListener {
            loadFragmentView(QRscanner())
        }
        qrGenerator.setOnClickListener {
            loadFragmentView(QRgenerator())
        }
        qrHistory.setOnClickListener {
            loadFragmentView(QRhistory())
        }
    }

    private fun loadFragmentView(fragment: Fragment?){
        if(fragment==null){
            //Log.d("BottomNavigation.loadFragmentView",fragment!!)
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container, QRscanner()
            ).commit()
        }else{
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,fragment
            ).commit()
        }
    }
}