package com.example.scannerapp.view.details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.util.copy
import com.example.scannerapp.R
import com.example.scannerapp.db.AppDatabase
import com.example.scannerapp.db.QRHistoryType
import com.example.scannerapp.service.QRGeneratorService
import com.example.scannerapp.service.QrType
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class DetailsActivity : AppCompatActivity() {
    private lateinit var scanTextvalue:TextView
    private lateinit var shareBtn:LinearLayout
    private lateinit var copyBtn:LinearLayout
    private lateinit var datetimetext:TextView
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
        val currentdateTime=LocalDateTime.now()
        val scannerValue=intent.getStringExtra("value")
        Log.d("value",scannerValue.toString())


        var title=findViewById<TextView>(R.id.qrTypeTitle)
        var qrtype=intent.getStringExtra("type")
        val scannerDateTime=intent.getStringExtra("datetime")
        var detailsType=intent.getStringExtra("detailsType")
        Log.d("detailsType",detailsType.toString())
        var saveqrBtn=findViewById<LinearLayout>(R.id.saveToGallery)
        var qrImage=findViewById<ImageView>(R.id.qrImageview)
        var qrImageViewLayout=findViewById<LinearLayout>(R.id.qrImageViewLayout)
        copyBtn=findViewById<LinearLayout>(R.id.copy)
        shareBtn=findViewById<LinearLayout>(R.id.share)
        var bitmap:Bitmap?=null
        if(detailsType==null){
            qrImageViewLayout.visibility=LinearLayout.INVISIBLE
        }else{
            bitmap=  QRGeneratorService().generateQR(inputValue = scannerValue.toString(), type = qrtype!!)
            qrImage.setImageBitmap(bitmap)

        }
        title.setText(qrtype)
        shareBtn=findViewById<LinearLayout>(R.id.share)
        copyBtn=findViewById<LinearLayout>(R.id.copy)
        datetimetext=findViewById<TextView>(R.id.dateTimevalue)
        saveqrBtn.setOnClickListener {
            QRGeneratorService().saveToGallery(context = this, bitmap = bitmap!!)
        }
        scanTextvalue=findViewById<TextView>(R.id.value)

        //date time format
        if(scannerDateTime==null){
            val dateTime = currentdateTime.format(formatter)
            datetimetext.setText("${dateTime}")
        }else{
            var formatTime=LocalDateTime.parse(scannerDateTime).format(formatter)
            datetimetext.setText("${formatTime}")

        }

        scanTextvalue.setText(scannerValue)

        copyText(scannerValue!!)
        share()


    }

    fun copyText(value:String){
        copyBtn.setOnClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clipData = android.content.ClipData.newPlainText("text", value)
            clipboardManager.setPrimaryClip(clipData)
        }

    }

    fun share(){
        shareBtn.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, scanTextvalue.text)
                type = "text/plain"
            }
            startActivity(sendIntent)
        }
    }
}