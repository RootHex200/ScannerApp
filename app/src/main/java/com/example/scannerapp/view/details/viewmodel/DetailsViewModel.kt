package com.example.scannerapp.view.details.viewmodel

import android.graphics.Bitmap
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.service.QrGeneratorServices
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val qrGeneratorService: QrGeneratorServices,
):BaseViewModel() {

    fun getQrGenerated(value:String,type:QrCodeType):Bitmap{
        return  qrGeneratorService.generateQrCode(value,type)
    }

    fun saveToGallery(bitmap:Bitmap){
        qrGeneratorService.saveToGallery(bitmap)
    }
}