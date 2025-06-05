package com.example.scannerapp.domain.repositories

import com.example.scannerapp.domain.model.QrCode

interface  QrCodeRepositories {

    fun getAllScannedQrCode():List<QrCode>

    fun getAllCreatedQrCode():List<QrCode>

    fun deleteQrCodeData(value:QrCode)

    fun saveQrCodeData(value:QrCode)
}