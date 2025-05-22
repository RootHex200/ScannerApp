package com.example.scannerapp.view.details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.scannerapp.R
import com.example.scannerapp.core.base.SimpleActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailsActivity : SimpleActivity() {

    private lateinit var scanTextvalue:TextView
    private lateinit var shareBtn:LinearLayout
    private lateinit var copyBtn:LinearLayout
    private lateinit var datetimetext:TextView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")

    override fun getLayout(): Int {
        return R.layout.activity_details
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
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
//            bitmap=  ServiceImpl().generateQR(inputValue = scannerValue.toString(), type = qrtype!!)
//            qrImage.setImageBitmap(bitmap)

        }
        title.setText(qrtype)
        shareBtn=findViewById<LinearLayout>(R.id.share)
        copyBtn=findViewById<LinearLayout>(R.id.copy)
        datetimetext=findViewById<TextView>(R.id.dateTimevalue)
        saveqrBtn.setOnClickListener {
//            ServiceImpl().saveToGallery(context = this, bitmap = bitmap!!)
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

    private fun copyText(value:String){
        copyBtn.setOnClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clipData = android.content.ClipData.newPlainText("text", value)
            clipboardManager.setPrimaryClip(clipData)
        }

    }

    private fun share(){
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