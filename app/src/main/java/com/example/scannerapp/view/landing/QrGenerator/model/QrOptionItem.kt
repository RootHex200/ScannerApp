package com.example.scannerapp.view.landing.QrGenerator.model

import com.example.scannerapp.service.QrType

data class QrOptionItem(
    var title:String,
    var image:Int,
    var qrOptionType:QrType
)