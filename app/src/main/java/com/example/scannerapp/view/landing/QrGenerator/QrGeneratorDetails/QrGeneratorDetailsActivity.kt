package com.example.scannerapp.view.landing.QrGenerator.QrGeneratorDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerapp.R
import com.example.scannerapp.service.QrType
import com.example.scannerapp.view.landing.QrGenerator.QrGeneratorOption.TextQrFragment

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
        var fragment=TextQrFragment()
        val mBundle = Bundle()
        mBundle.putString(
            "value",
            value
        )
        fragment.setArguments(mBundle)
        if(value==QrType.TEXT.name || value==QrType.PHONE.name ||
            value==QrType.SMS.name || value==QrType.EMAIL.name ||
            value==QrType.LOCATION.name || value==QrType.CONTACT.name){
            supportFragmentManager.beginTransaction().replace(
                R.id.detailsFrameLayout,
                fragment

            ).commit()
        }
    }
}