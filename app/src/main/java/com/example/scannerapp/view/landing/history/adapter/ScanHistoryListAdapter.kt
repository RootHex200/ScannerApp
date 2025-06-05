package com.example.scannerapp.view.landing.history.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.view.details.DetailsActivity
import com.example.scannerapp.view.landing.history.viewmodel.HistoryViewModel

class ScanHistoryListAdapter(
    var historyList:MutableList<QrCode>,
    var context: Context?,
    var viewModel: HistoryViewModel
): RecyclerView.Adapter<ScanHistoryListAdapter.ViewHolder>() {

    fun updateData(newList: List<QrCode>) {
        historyList.clear()  // Clear the old list
        historyList.addAll(newList)
        notifyDataSetChanged()// Add new data

    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var text=view.findViewById<TextView>(R.id.linkText)
        var data=view.findViewById<TextView>(R.id.data)
        var deleteBtn=view.findViewById<ImageView>(R.id.deleteBtn)
        //var dateTimeview=view.findViewById<TextView>(R.id.dateTime)
        var layout=view.findViewById<View>(R.id.historyItem)


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

        holder.text.setText(historyList[position].type.toString())
        holder.data.setText(historyList[position].content)

        holder.deleteBtn.setOnClickListener {
            Log.d("historyButtonClicked","${position}${historyList[position].isScanned.toString()}")
            if(historyList[position].isScanned){
                viewModel.deleteScanQrCode(historyList[position])
            }else{
                viewModel.deleteCreatedQrCode(historyList[position])
            }
        }

        holder.layout.setOnClickListener {
            var intent=Intent(context,DetailsActivity::class.java)
            intent.putExtra("value",historyList[position].content)
            intent.putExtra("datetime",historyList[position].createdAt)
            intent.putExtra("type",historyList[position].type)
            if(historyList[position].isScanned==false){
                intent.putExtra("detailsType","createHistory")
            }
            context!!.startActivity(intent)

        }
    }
}