package com.example.scannerapp.service

import android.graphics.Bitmap
import com.example.scannerapp.core.common.RequestCompleteListener
import com.example.scannerapp.core.common.model.QrScanValueModel
import com.example.scannerapp.domain.model.QrCode
import com.google.mlkit.vision.common.InputImage

interface QrScannerService {
    fun scanQrCodeFromImage(image: InputImage, callback:RequestCompleteListener<QrCode>)
}