package com.example.scannerapp.core.common.model

import com.example.scannerapp.domain.model.QrCodeType

data class QrScanValueModel(
    val content:String,
    val type:QrCodeType,
    val rawValue:String
)