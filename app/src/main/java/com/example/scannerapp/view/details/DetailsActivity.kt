package com.example.scannerapp.view.details

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.scannerapp.R
import com.example.scannerapp.db.AppDatabase
import com.example.scannerapp.db.QRHistoryType
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
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
        val currentdateTime=LocalDateTime.now()
        val scannerValue=intent.getStringExtra("value")
        val scannerDateTime=intent.getStringExtra("datetime")
        var detailsType=intent.getStringExtra("detailsType")
        shareBtn=findViewById<LinearLayout>(R.id.share)
        copyBtn=findViewById<LinearLayout>(R.id.copy)
        datetimetext=findViewById<TextView>(R.id.dateTimevalue)

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




    }
}