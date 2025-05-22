package com.example.scannerapp.view.landing.QrGenerator.QrGeneratorDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerapp.R
import com.example.scannerapp.core.base.SimpleActivity
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.view.landing.QrGenerator.QrGeneratorOption.TextQrFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrGeneratorDetailsActivity : SimpleActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var detailsFrameLayout:FrameLayout

    override fun getLayout(): Int {
        return  R.layout.activity_qr_generator_details
    }

    override fun init() {
        var qrType=intent.getStringExtra("qrType")
        Log.d("QrGeneratorDetailsActivity.changeLayoutByValue",qrType!!.toString())
        changeLayoutByValue(value = qrType!!)

    }

    private  fun changeLayoutByValue(value:String){
        Log.d("QrGeneratorDetailsActivity.changeLayoutByValue",value)
        var fragment=TextQrFragment()
        val mBundle = Bundle()
        mBundle.putString(
            "value",
            value
        )
        fragment.setArguments(mBundle)
        if(value== QrCodeType.TEXT.name || value==QrCodeType.PHONE.name ||
            value==QrCodeType.SMS.name || value==QrCodeType.EMAIL.name ||
            value==QrCodeType.LOCATION.name || value==QrCodeType.CONTACT.name){
            supportFragmentManager.beginTransaction().replace(
                R.id.detailsFrameLayout,
                fragment

            ).commit()
        }
    }
}