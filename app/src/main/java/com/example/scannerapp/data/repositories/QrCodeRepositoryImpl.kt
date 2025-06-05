package com.example.scannerapp.data.repositories

import android.util.Log
import com.example.scannerapp.core.db.QrCodeInfo
import com.example.scannerapp.data.data_source.QrCodeDataSource
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.repositories.QrCodeRepositories
import javax.inject.Inject

class QrCodeRepositoryImpl @Inject constructor(
    private val localDataSource: QrCodeDataSource
) :QrCodeRepositories{
    override fun getAllScannedQrCode(): List<QrCode> {
        return localDataSource.getQrScannedData().map { it->
             QrCode.fromModel(it)

        }.toList()
    }

    override fun getAllCreatedQrCode(): List<QrCode> {
        return localDataSource.getQrCreatedData().map { it->
            QrCode.fromModel(it)
        }.toList()
    }

    override fun deleteQrCodeData(value: QrCode) {
        localDataSource.deleteQrInfo(QrCodeInfo.toModel(value))
    }

    override fun saveQrCodeData(value: QrCode) {
        localDataSource.saveQrInfo(QrCodeInfo.toModel(value))
    }
}