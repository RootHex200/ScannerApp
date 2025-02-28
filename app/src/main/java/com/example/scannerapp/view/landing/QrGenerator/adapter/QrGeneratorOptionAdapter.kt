package com.example.scannerapp.view.landing.QrGenerator.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.view.landing.QrGenerator.model.QrOptionItem


class QrGeneratorOptionAdapter(private var data:List<QrOptionItem>) : RecyclerView.Adapter<QrGeneratorOptionAdapter.ViewHolder>() {


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
        holder.image.setImageResource(data[position].image)
        holder.btn.setOnClickListener {
            Log.d("QrGeneratorOptionAdapter.setOnClickListener","btn")
        }
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var title=view.findViewById<TextView>(R.id.itemTitle)
        var image=view.findViewById<ImageView>(R.id.itemImage)
        var btn=view.findViewById<LinearLayout>(R.id.itemBtn)
    }

}