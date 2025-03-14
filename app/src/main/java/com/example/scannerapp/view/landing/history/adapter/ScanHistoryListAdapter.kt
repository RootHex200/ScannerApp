package com.example.scannerapp.view.landing.history.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.db.QRHistoryDao
import com.example.scannerapp.db.QRHistoryInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScanHistoryListAdapter(
    var historyList:List<QRHistoryInfo>,
    var qrHistoryDao: QRHistoryDao): RecyclerView.Adapter<ScanHistoryListAdapter.ViewHolder>() {


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var text=view.findViewById<TextView>(R.id.linkText)
        var data=view.findViewById<TextView>(R.id.data)
        var deleteBtn=view.findViewById<ImageView>(R.id.deleteBtn)
        var dateTimeview=view.findViewById<TextView>(R.id.dateTime)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var view:View=LayoutInflater.from(parent.context).inflate(R.layout.scan_history_item_view,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var parse=LocalDateTime.parse(historyList[position].createAt)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
        val formateDate = parse.format(formatter)

        holder.dateTimeview.setText(formateDate)
        holder.text.setText("Data")
        holder.data.setText(historyList[position].value)

        holder.deleteBtn.setOnClickListener {
            qrHistoryDao.delete(historyList[position])
        }
    }
}