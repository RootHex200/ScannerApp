package com.example.scannerapp.view.landing.history.scanHistory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R

class ScanHistoryListAdapter: RecyclerView.Adapter<ScanHistoryListAdapter.ViewHolder>() {


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var text=view.findViewById<TextView>(R.id.linkText)
        var data=view.findViewById<TextView>(R.id.data)
        var deleteBtn=view.findViewById<ImageView>(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var view:View=LayoutInflater.from(parent.context).inflate(R.layout.scan_history_item_view,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.setText("sabitur rahman ${position}")
    }
}