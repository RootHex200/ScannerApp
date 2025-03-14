package com.example.scannerapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QRHistoryType{
    CREATE_HISTORY,
    SCAN_HISTORY
}


@Entity
data class QRHistoryInfo(
    @PrimaryKey(autoGenerate = true) val uid:Long=0,
    @ColumnInfo("history_type") val historyType:QRHistoryType?,
    @ColumnInfo("create_at") val createAt:String?,
    @ColumnInfo("value") val value:String?,
    @ColumnInfo("value_type") val type:String?
)
