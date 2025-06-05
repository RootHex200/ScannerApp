package com.example.scannerapp.data.data_source

import com.example.scannerapp.core.db.QrCodeDeo
import com.example.scannerapp.core.db.QrCodeInfo
import javax.inject.Inject

class QrCodeLocalDataSource @Inject constructor(
    private val qrCodeDao: QrCodeDeo
) :QrCodeDataSource {

    override fun getQrCreatedData():List<QrCodeInfo> {
       return qrCodeDao.getQrcodeCreatedInfo()
    }

    override fun getQrScannedData():List<QrCodeInfo> {
       return qrCodeDao.getQrcodeScannedInfo()
    }

    override fun saveQrInfo(value:QrCodeInfo) {
        qrCodeDao.insertQRInfo(value)
    }

    override fun deleteQrInfo(value:QrCodeInfo) {
        qrCodeDao.delete(value)
    }
}