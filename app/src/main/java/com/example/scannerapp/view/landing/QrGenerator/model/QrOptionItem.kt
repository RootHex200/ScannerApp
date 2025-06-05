package com.example.scannerapp.view.landing.QrGenerator.model

import com.example.scannerapp.domain.model.QrCodeType

data class QrOptionItem(
    var title:String,
    var image:Int,
    var qrOptionType:QrCodeType
)