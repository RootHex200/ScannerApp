package com.example.scannerapp.service

import android.content.Context
import android.graphics.Bitmap
import com.example.scannerapp.domain.model.QrCodeType

interface QrGeneratorServices {
    /*
        `generateQrCode` is a function that takes a string as input and returns a qr code `Bitmap`.
     */
    fun generateQrCode(data: String,type:QrCodeType):Bitmap

    /*
        `saveToGallery` is a function that takes a `Bitmap` as input and saves it to the device's gallery.
     */
    fun saveToGallery( bitmap: Bitmap)
}