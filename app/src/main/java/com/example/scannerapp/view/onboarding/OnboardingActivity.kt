package com.example.scannerapp.view.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scannerapp.R
import com.example.scannerapp.core.base.SimpleActivity
import com.example.scannerapp.view.landing.BottomNavigation
import dagger.hilt.android.AndroidEntryPoint

class OnboardingActivity : SimpleActivity() {
    private lateinit var getStartButton:ImageButton

    override fun getLayout(): Int {
        return R.layout.activity_onboarding
    }

    override fun init() {
        getStartButton=findViewById<ImageButton>(R.id.btn)
        getStartButton.setOnClickListener {
            var intent=Intent(this,BottomNavigation::class.java)
            startActivity(intent)
        }
    }
}