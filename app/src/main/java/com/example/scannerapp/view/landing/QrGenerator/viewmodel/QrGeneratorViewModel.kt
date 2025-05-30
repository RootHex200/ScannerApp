package com.example.scannerapp.view.landing.QrGenerator.viewmodel

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.domain.repositories.QrCodeRepositories
import com.example.scannerapp.service.QrGeneratorServices
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class QrGeneratorViewModel  @Inject constructor(
    private val qrGeneratorService: QrGeneratorServices,
    private val qrCodeRepository: QrCodeRepositories
): BaseViewModel() {


    fun saveQrCodeToGallery(bitmap: Bitmap) {
        qrGeneratorService.saveToGallery(bitmap)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateQrCode(data: String, type: QrCodeType): Bitmap {
        qrCodeRepository.saveQrCodeData(QrCode(content = data, type =type, isScanned = false, createdAt = LocalTime.now().toString() ))
        return  qrGeneratorService.generateQrCode(data,type)
    }


}