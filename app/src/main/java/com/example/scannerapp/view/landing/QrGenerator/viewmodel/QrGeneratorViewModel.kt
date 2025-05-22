package com.example.scannerapp.view.landing.QrGenerator.viewmodel

import android.content.Context
import android.graphics.Bitmap
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.service.QrGeneratorServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class QrGeneratorViewModel  @Inject constructor(
    private val qrGeneratorService: QrGeneratorServices,
): BaseViewModel() {


    fun saveQrCode(bitmap: Bitmap) {
        qrGeneratorService.saveToGallery(bitmap)
    }

    fun generateQrCode(data: String,type: QrCodeType): Bitmap {
        return  qrGeneratorService.generateQrCode(data,type)
    }
}