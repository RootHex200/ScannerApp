package com.example.scannerapp.service

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.scannerapp.core.common.RequestCompleteListener
import com.example.scannerapp.core.common.model.QrScanValueModel
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.model.QrCodeType
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.time.LocalDateTime

class QrScannerServiceImpl:QrScannerService {
    @RequiresApi(Build.VERSION_CODES.O)
    private val localTime:String=LocalDateTime.now().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun scanQrCodeFromImage(image: InputImage,callback: RequestCompleteListener<QrCode>) {

        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val qrCode = formatBarcode(barcodes[0])
                    callback.onSuccess(qrCode)
                } else {
                    callback.onFailure("No QR code found in the image")
                }
            }
            .addOnFailureListener { e ->
                callback.onFailure(e.toString())
            }
    }


        //QR data formator service
        @RequiresApi(Build.VERSION_CODES.O)
       private fun formatBarcode(barcode: Barcode): QrCode {
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatWifi(barcode: Barcode): QrCode {
        val wifi = barcode.wifi ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Network: ${wifi.ssid}")
            appendLine("Password: ${wifi.password}")
            appendLine("Encryption Type: ${getEncryptionType(wifi.encryptionType)}")
        }
        return QrCode(type = QrCodeType.WIFI, content =  formattedContent, createdAt = localTime)
    }

    private fun getEncryptionType(type: Int): String {
        return when (type) {
            Barcode.WiFi.TYPE_OPEN -> "Open"
            Barcode.WiFi.TYPE_WPA -> "WPA"
            Barcode.WiFi.TYPE_WEP -> "WEP"
            else -> "Unknown"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatUrl(barcode: Barcode): QrCode {
        val url = barcode.url ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("URL: ${url.url}")
            if (!url.title.isNullOrEmpty()) {
                appendLine("Title: ${url.title}")
            }
        }
        return QrCode(type = QrCodeType.URL, content =  formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatEmail(barcode: Barcode): QrCode {
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
        return QrCode(type = QrCodeType.valueOf("EMAIL"), content =  formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatContact(barcode: Barcode): QrCode {
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
        return QrCode(type = QrCodeType.CONTACT,content= formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatSms(barcode: Barcode): QrCode {
        val sms = barcode.sms ?: return formatPlainText(barcode)
        val formattedContent = buildString {
            appendLine("Phone Number: ${sms.phoneNumber}")
            appendLine("Message: ${sms.message}")
        }
        return QrCode(type = QrCodeType.SMS, content =  formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatPhone(barcode: Barcode): QrCode {
        val phone = barcode.phone ?: return formatPlainText(barcode)
        return QrCode(type = QrCodeType.PHONE, content = "Phone Number: ${phone.number}", createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatGeo(barcode: Barcode): QrCode {
        val geo = barcode.geoPoint ?: return formatPlainText(barcode)
        val formattedContent = "Location: ${geo.lat}, ${geo.lng}"
        return QrCode(type = QrCodeType.GEO, content =  formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatCalendarEvent(barcode: Barcode): QrCode {
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
        return QrCode(type = QrCodeType.CALENDAR,content =formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDriverLicense(barcode: Barcode): QrCode {
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
        return QrCode(type = QrCodeType.DRIVER_LICENSE, content =  formattedContent, createdAt = localTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatPlainText(barcode: Barcode): QrCode {
        // Try to determine if this is a specific format not detected by the scanner
        val rawValue = barcode.rawValue ?: ""

        // Check for cryptocurrency addresses or other custom formats
        return when {
            // Basic Bitcoin address detection
            rawValue.matches(Regex("^(bc1|[13])[a-zA-HJ-NP-Z0-9]{25,39}$")) -> {
                QrCode(type = QrCodeType.BITCOIN, content =  "Bitcoin Address: $rawValue", createdAt = localTime)
            }
            // Ethereum address detection
            rawValue.matches(Regex("^0x[a-fA-F0-9]{40}$")) -> {
                QrCode(type = QrCodeType.ETHEREUM, content =  "Ethereum Address: $rawValue", createdAt = localTime)
            }
            // Check if it's a URL even if not detected as such
            rawValue.startsWith("http://") || rawValue.startsWith("https://") -> {
                QrCode(type = QrCodeType.URL, content =  "URL: $rawValue", createdAt = localTime)
            }
            // Default plain text
            else -> {
                QrCode(type = QrCodeType.TEXT, content = rawValue, createdAt = localTime)
            }
        }
    }

}