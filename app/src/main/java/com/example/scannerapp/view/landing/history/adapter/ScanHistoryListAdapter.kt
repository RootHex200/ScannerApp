package com.example.scannerapp.view.landing.history.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.db.QRHistoryInfo
import com.example.scannerapp.view.landing.history.viewmodel.ScanHistoryViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScanHistoryListAdapter(
    var historyList:MutableList<QRHistoryInfo>,
    var context: Context?,
    var viewModel: ScanHistoryViewModel
): RecyclerView.Adapter<ScanHistoryListAdapter.ViewHolder>() {

    fun updateData(newList: List<QRHistoryInfo>) {
        historyList.clear()  // Clear the old list
        historyList.addAll(newList)  // Add new data
        notifyDataSetChanged()  // Notify adapter of changes
    }

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
            viewModel.deleteHistory(historyList[position],context!!)
        }
    }
}