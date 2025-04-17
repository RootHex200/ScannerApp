package com.example.scannerapp.service

import android.R.attr.bitmap
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Picture
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream



class QRGeneratorService:QrServiceRepository {

    private fun getQrType(type:QrType):String{
        if (type==QrType.SMS){
            return QRGContents.Type.SMS;
        }
        if(type==QrType.EMAIL){
            return QRGContents.Type.EMAIL;
        }
        if(type==QrType.CONTACT){
            return QRGContents.Type.CONTACT;
        }
        if(type==QrType.PHONE){
            return QRGContents.Type.PHONE;
        }
        if(type==QrType.LOCATION){
            return QRGContents.Type.LOCATION;
        }

        return QRGContents.Type.TEXT;
    }


    override fun generateQR(inputValue:String,type:QrType):Bitmap{
        val qrType=getQrType(type)

        val qrgEncoder = QRGEncoder(inputValue, null, qrType,200)
        try {
            // Getting QR-Code as Bitmap
            var bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            return bitmap;
        } catch (e:Exception) {
            throw Exception("Generate QR image error")
        }
    }


    override fun saveToGallery(context: Context, bitmap: Bitmap) {
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

            context.contentResolver.let {
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
    }



    //QR data formator service
    override fun formatBarcode(barcode: Barcode): QRData {
        return when (barcode.valueType) {
            Barcode.TYPE_WIFI -> formatWifi(barcode)
            Barcode.TYPE_URL -> formatUrl(barcode)
            Barcode.TYPE_EMAIL -> formatEmail(barcode)
            Barcode.TYPE_CONTACT_INFO -> formatContact(barcode)
            Barcode.TYPE_SMS -> formatSms(barcode)
            Barcode.TYPE_PHONE -> formatPhone(barcode)
            Barcode.TYPE_GEO -> formatGeo(barcode)
            Barcode.TYPE_CALENDAR_EVENT -> formatCalendarEvent(barcode)
            Barcode.TYPE_DRIVER_LICENSE -> formatDriverLicense(barcode)
            else -> formatPlainText(barcode)
        }
    }

    private fun formatWifi(barcode: Barcode): QRData {
        val wifi = barcode.wifi ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Network: ${wifi.ssid}")
            appendLine("Password: ${wifi.password}")
            appendLine("Encryption Type: ${getEncryptionType(wifi.encryptionType)}")
        }
        return QRData("WIFI", formattedContent, barcode.rawValue ?: "")
    }

    private fun getEncryptionType(type: Int): String {
        return when (type) {
            Barcode.WiFi.TYPE_OPEN -> "Open"
            Barcode.WiFi.TYPE_WPA -> "WPA"
            Barcode.WiFi.TYPE_WEP -> "WEP"
            else -> "Unknown"
        }
    }

    private fun formatUrl(barcode: Barcode): QRData {
        val url = barcode.url ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("URL: ${url.url}")
            if (!url.title.isNullOrEmpty()) {
                appendLine("Title: ${url.title}")
            }
        }
        return QRData("URL", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatEmail(barcode: Barcode): QRData {
        val email = barcode.email ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Email: ${email.address}")
            if (!email.subject.isNullOrEmpty()) {
                appendLine("Subject: ${email.subject}")
            }
            if (!email.body.isNullOrEmpty()) {
                appendLine("Body: ${email.body}")
            }
        }
        return QRData("EMAIL", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatContact(barcode: Barcode): QRData {
        val contact = barcode.contactInfo ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            if (contact.name != null) {
                val name = contact.name!!
                appendLine("Name: ${name.formattedName}")
            }

            if (contact.phones.isNotEmpty()) {
                appendLine("Phone Numbers:")
                contact.phones.forEach { phone ->
                    appendLine("  ${phone.type ?: "Default"}: ${phone.number}")
                }
            }

            if (contact.emails.isNotEmpty()) {
                appendLine("Email Addresses:")
                contact.emails.forEach { email ->
                    appendLine("  ${email.type ?: "Default"}: ${email.address}")
                }
            }

            if (contact.addresses.isNotEmpty()) {
                appendLine("Addresses:")
                contact.addresses.forEach { address ->
                    appendLine("  ${address.type ?: "Default"}: ${address.addressLines.joinToString(", ")}")
                }
            }

            if (!contact.organization.isNullOrEmpty()) {
                appendLine("Organization: ${contact.organization}")
            }

            if (!contact.title.isNullOrEmpty()) {
                appendLine("Title: ${contact.title}")
            }

            if (!contact.urls.isNullOrEmpty()) {
                appendLine("URLs:")
                contact.urls.forEach { url ->
                    appendLine("  $url")
                }
            }
        }
        return QRData("CONTACT", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatSms(barcode: Barcode): QRData {
        val sms = barcode.sms ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Phone Number: ${sms.phoneNumber}")
            appendLine("Message: ${sms.message}")
        }
        return QRData("SMS", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatPhone(barcode: Barcode): QRData {
        val phone = barcode.phone ?: return formatPlainText(barcode)
        return QRData("PHONE", "Phone Number: ${phone.number}", barcode.rawValue ?: "")
    }

    private fun formatGeo(barcode: Barcode): QRData {
        val geo = barcode.geoPoint ?: return formatPlainText(barcode)
        val formattedContent = "Location: ${geo.lat}, ${geo.lng}"
        return QRData("GEO", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatCalendarEvent(barcode: Barcode): QRData {
        val event = barcode.calendarEvent ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Summary: ${event.summary}")
            appendLine("Description: ${event.description}")
            appendLine("Location: ${event.location}")
            appendLine("Start: ${event.start?.rawValue}")
            appendLine("End: ${event.end?.rawValue}")
            appendLine("Organizer: ${event.organizer}")
            if (event.status != null) {
                appendLine("Status: ${event.status}")
            }
        }
        return QRData("CALENDAR", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatDriverLicense(barcode: Barcode): QRData {
        val license = barcode.driverLicense ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Document Type: Driver's License")
            appendLine("License Number: ${license.licenseNumber}")
            appendLine("Name: ${license.firstName} ${license.middleName} ${license.lastName}")
            appendLine("Gender: ${license.gender}")
            appendLine("Address: ${license.addressStreet}, ${license.addressCity}, ${license.addressState} ${license.addressZip}")
            appendLine("Birth Date: ${license.birthDate}")
            appendLine("Issue Date: ${license.issueDate}")
            appendLine("Expiry Date: ${license.expiryDate}")
            appendLine("Issuing Country: ${license.issuingCountry}")
        }
        return QRData("DRIVER_LICENSE", formattedContent, barcode.rawValue ?: "")
    }

    private fun formatPlainText(barcode: Barcode): QRData {
        // Try to determine if this is a specific format not detected by the scanner
        val rawValue = barcode.rawValue ?: ""

        // Check for cryptocurrency addresses or other custom formats
        return when {
            // Basic Bitcoin address detection
            rawValue.matches(Regex("^(bc1|[13])[a-zA-HJ-NP-Z0-9]{25,39}$")) -> {
                QRData("BITCOIN", "Bitcoin Address: $rawValue", rawValue)
            }
            // Ethereum address detection
            rawValue.matches(Regex("^0x[a-fA-F0-9]{40}$")) -> {
                QRData("ETHEREUM", "Ethereum Address: $rawValue", rawValue)
            }
            // Check if it's a URL even if not detected as such
            rawValue.startsWith("http://") || rawValue.startsWith("https://") -> {
                QRData("URL", "URL: $rawValue", rawValue)
            }
            // Default plain text
            else -> {
                QRData("TEXT", rawValue, rawValue)
            }
        }
    }
}
