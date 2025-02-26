package com.example.scannerapp.view.landing

import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.scannerapp.R

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
                R.id.fragment_container,QRscanner()
            ).commit()
        }else{
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,fragment
            ).commit()
        }
    }
}