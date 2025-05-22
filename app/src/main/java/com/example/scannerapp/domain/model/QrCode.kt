package com.example.scannerapp.domain.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.scannerapp.core.db.QrCodeInfo
import java.time.LocalDateTime

enum class QrCodeType {
    TEXT, EMAIL, PHONE, CONTACT, SMS, LOCATION,BITCOIN,ETHEREUM,URL,DRIVER_LICENSE,CALENDAR,GEO,WIFI
}

data class QrCode(
    val id: Long = 0,
    val content: String,
    val type: QrCodeType,
    val createdAt: String,
    val isScanned: Boolean = true
){
    companion object{
        fun fromModel(model:QrCodeInfo):QrCode{
            return QrCode(
                id = model.uid,
                content = model.value!!,
                type = QrCodeType.valueOf(model.type.toString()),
                createdAt = model.createAt!!,
                isScanned = model.historyType
            )
        }


    }
}