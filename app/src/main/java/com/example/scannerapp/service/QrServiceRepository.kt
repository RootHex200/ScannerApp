package com.example.scannerapp.service

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.common.Barcode

interface QrServiceRepository {
    // This method for generate QR code
    // [generateQR] function will return `Bitmap` of qr code image
    fun generateQR(inputValue:String,type:String):Bitmap

    //This method used for qr code data formator
    // [formatBarcode] function will return `QRData` object
    fun formatBarcode(barcode: Barcode): QRData

    //This method used for save qr code to gallery
    //This function responsible for save qr code to gallery
    fun saveToGallery(context: Context, bitmap: Bitmap)


}


/**
 * Data class to hold formatted QR code data
 * @param type The type of QR data (e.g., WIFI, URL, TEXT)
 * @param formattedData Human-readable formatted data
 * @param rawData The original raw data from the QR code
 */
data class QRData(
    val type: String,
    val formattedData: String,
    val rawData: String
)



// type of qr
enum class QrType{
    TEXT,
    PHONE,
    EMAIL,
    CONTACT,
    SMS,
    LOCATION
}