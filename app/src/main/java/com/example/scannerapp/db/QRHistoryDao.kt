package com.example.scannerapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface QRHistoryDao {
    @Query("SELECT * FROM qrhistoryinfo")
    fun getQRHistoryList(): List<QRHistoryInfo>

    @Insert
    fun insertQRInfo(vararg qrHistoryInfo: QRHistoryInfo)

    @Delete
    fun delete(qrHistoryInfo: QRHistoryInfo)
}