package com.example.scannerapp.view.landing.QrGenerator.QrGeneratorOption

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.scannerapp.R
import com.example.scannerapp.core.base.BaseFragment
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.view.landing.QrGenerator.viewmodel.QrGeneratorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextQrFragment() : BaseFragment<QrGeneratorViewModel>(QrGeneratorViewModel::class.java) {

    private lateinit var qrImageBitmap:Bitmap

    override fun getLayout(): Int {
        return R.layout.fragment_text_qr
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {

        // catch value from bundle
        val bundle = arguments
        val qrtype = bundle!!.getString("value")

        var textValue=rootView.findViewById<EditText>(R.id.textInput);
        var generateQrButton=rootView.findViewById<LinearLayout>(R.id.generateQrButton);
        var qrImageview=rootView.findViewById<ImageView>(R.id.qrImageview);
        var savePhoto=rootView.findViewById<LinearLayout>(R.id.saveToGallery);
        var pageTitles=rootView.findViewById<TextView>(R.id.pageTitle);
        var inputName=rootView.findViewById<TextView>(R.id.inputName);
        savePhoto.visibility=View.GONE
        pageTitles.setText(qrtype)
        inputName.setText("${qrtype?.lowercase()?.capitalize()} Input")
        generateQrButton.setOnClickListener {
            Log.d("Message","Click is here")
            val getBitmap=viewModel.generateQrCode(textValue.text.trim().toString() , QrCodeType.valueOf(qrtype!!))
            qrImageBitmap=getBitmap;
            qrImageview.setImageBitmap(getBitmap);
            savePhoto.visibility=View.VISIBLE



        }
        savePhoto.setOnClickListener {
            viewModel.saveQrCode(qrImageBitmap)
        }

    }
}