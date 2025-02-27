package com.example.scannerapp.view.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.view.landing.model.QrOptionItem


class QrGeneratorOptionList(private var data:List<QrOptionItem>) : RecyclerView.Adapter<QrGeneratorOptionList.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.qr_generate_option_item, parent, false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(data[position].title)
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var title=view.findViewById<TextView>(R.id.itemTitle)
        var image=view.findViewById<ImageView>(R.id.itemImage)
    }

}