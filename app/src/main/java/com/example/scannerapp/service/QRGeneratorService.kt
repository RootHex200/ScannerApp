package com.example.scannerapp.service

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder


class QRGeneratorService {


    fun generateQR(inputValue:String,Qrtype:String):Bitmap{

        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = QRGEncoder(inputValue, null, Qrtype,200)
        try {
            // Getting QR-Code as Bitmap
            var bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            return bitmap;
        } catch (e:Exception) {
            throw Exception("Generate QR image error")
        }
    }
}