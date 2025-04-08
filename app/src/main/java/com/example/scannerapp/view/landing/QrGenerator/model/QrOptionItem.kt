package com.example.scannerapp.view.landing.QrGenerator.model

data class QrOptionItem(
    var title:String,
    var image:Int,
    var QrOptionType:QrOptionType
)


enum class QrOptionType{
    Wifi,
    Web,
    Business,
    Text,
    Event,
    Contact
}