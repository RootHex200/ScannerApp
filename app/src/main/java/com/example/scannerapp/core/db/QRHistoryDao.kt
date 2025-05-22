package com.example.scannerapp.core.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface QrCodeDeo {
    @Query("SELECT * FROM qrcodeinfo where is_scanned=1")
    fun getQrcodeScannedInfo(): List<QrCodeInfo>

    @Query("SELECT * FROM qrcodeinfo where is_scanned=0")
    fun getQrcodeCreatedInfo(): List<QrCodeInfo>

    @Insert
    fun insertQRInfo(vararg qrcode: QrCodeInfo)

    @Delete
    fun delete(qrcode: QrCodeInfo)
}