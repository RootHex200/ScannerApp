package com.example.scannerapp.service

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.example.scannerapp.domain.model.QrCodeType
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class QrGeneratorServiceImpl(
    private val context: Context
):QrGeneratorServices {
    private fun getQrType(type:QrCodeType):String{
        if (type==QrCodeType.SMS){
            return QRGContents.Type.SMS;
        }
        if(type==QrCodeType.EMAIL){
            return QRGContents.Type.EMAIL;
        }
        if(type==QrCodeType.CONTACT){
            return QRGContents.Type.CONTACT;
        }
        if(type==QrCodeType.PHONE){
            return QRGContents.Type.PHONE;
        }
        if(type==QrCodeType.LOCATION){
            return QRGContents.Type.LOCATION;
        }

        return QRGContents.Type.TEXT;
    }
    override fun generateQrCode(data: String,type:QrCodeType): Bitmap {
        val qrgEncoder = QRGEncoder(data, null, getQrType(type),200)
        try {
            // Getting QR-Code as Bitmap
            var bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            return bitmap;
        } catch (e:Exception) {
            throw Exception("Generate QR image error")
        }
    }

    override fun saveToGallery(bitmap: Bitmap) {
        try {
            val filename = "${System.currentTimeMillis()}.png"
            val write: (OutputStream) -> Boolean = {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/Picture")
                }

                context.applicationContext.contentResolver.let {
                    it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                        it.openOutputStream(uri)?.let(write)
                    }
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Picture"
                val file = File(imagesDir)
                if (!file.exists()) {
                    file.mkdir()
                }
                val image = File(imagesDir, filename)
                write(FileOutputStream(image))
            }
        }catch (e:Exception){
            throw Exception("Save to gallery error ${e.toString()}")
        }
    }
}