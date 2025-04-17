package com.example.scannerapp.view.QrGeneratorDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerapp.R
import com.example.scannerapp.view.landing.QrGenerator.QrGeneratorOption.TextQrFragment
import com.example.scannerapp.view.landing.QrGenerator.model.QrOptionType

class QrGeneratorDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var detailsFrameLayout:FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_generator_details)
        var qrType=intent.getStringExtra("qrType")
        Log.d("QrGeneratorDetailsActivity.changeLayoutByValue",qrType!!.toString())
        changeLayoutByValue(value = qrType!!)

    }

    fun changeLayoutByValue(value:String){
        Log.d("QrGeneratorDetailsActivity.changeLayoutByValue",value)
        if (value==QrOptionType.Wifi.name){
            supportFragmentManager.beginTransaction().replace(
                R.id.detailsFrameLayout,TextQrFragment()
            ).commit()
        }
        if(value==QrOptionType.Web.name){

        }
    }
}