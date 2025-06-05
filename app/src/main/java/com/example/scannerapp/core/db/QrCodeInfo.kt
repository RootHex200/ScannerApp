package com.example.scannerapp.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.model.QrCodeType

@Entity
data class QrCodeInfo(
    @PrimaryKey(autoGenerate = true) val uid:Long=0,
    @ColumnInfo("is_scanned") val historyType: Boolean,
    @ColumnInfo("create_at") val createAt:String?,
    @ColumnInfo("content") val value:String?,
    @ColumnInfo("type") val type:QrCodeType?
){
    companion object{
        fun toModel(qrCode: QrCode):QrCodeInfo{
            return  QrCodeInfo(
                uid = qrCode.id,
                historyType = qrCode.isScanned,
                createAt = qrCode.createdAt,
                value = qrCode.content,
                type = QrCodeType.valueOf(qrCode.type.toString())
            );
        }
    }
}
