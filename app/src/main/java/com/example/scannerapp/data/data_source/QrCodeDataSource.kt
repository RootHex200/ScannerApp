package com.example.scannerapp.data.data_source

import com.example.scannerapp.core.db.QrCodeInfo

interface QrCodeDataSource {

    fun getQrCreatedData():List<QrCodeInfo>

    fun getQrScannedData():List<QrCodeInfo>

    fun saveQrInfo(value:QrCodeInfo)

    fun deleteQrInfo(value: QrCodeInfo)
}