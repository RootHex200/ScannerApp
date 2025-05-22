package com.example.scannerapp.view.landing.scanner

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.core.common.RequestCompleteListener
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.repositories.QrCodeRepositories
import com.example.scannerapp.service.QrScannerService
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val qrCodeRepository: QrCodeRepositories,
    private val qrScannerService: QrScannerService
):BaseViewModel() {

    val scanQrSuccess=BehaviorSubject.create<QrCode>()
    val scanQrFailure=BehaviorSubject.create<String>()
    fun qrScanFromImage(image: InputImage){

        qrScannerService.scanQrCodeFromImage(image, callback =  object : RequestCompleteListener<QrCode> {
            override fun onSuccess(data: QrCode) {
                saveQrCode(data)
                scanQrSuccess.onNext(data)
            }
            override fun onFailure(errorMessage: String) {
                scanQrFailure.onNext(errorMessage)
            }

        })
    }

    fun saveQrCode(value: QrCode){
        qrCodeRepository.saveQrCodeData(value)

    }
}