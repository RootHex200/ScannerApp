package com.example.scannerapp.view.landing.QrGenerator.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.view.QrGeneratorDetails.QrGeneratorDetailsActivity
import com.example.scannerapp.view.landing.QrGenerator.model.QrOptionItem
import com.example.scannerapp.view.landing.history.scanHistory.ScanHistory


class QrGeneratorOptionAdapter(private var data:List<QrOptionItem>) : RecyclerView.Adapter<QrGeneratorOptionAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
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
            var intent=Intent(context,QrGeneratorDetailsActivity::class.java)
            intent.putExtra("qrType",data[position].QrOptionType.name)
            startActivity(context,intent,null)
        }
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var title=view.findViewById<TextView>(R.id.itemTitle)
        var image=view.findViewById<ImageView>(R.id.itemImage)
        var btn=view.findViewById<LinearLayout>(R.id.itemBtn)
    }

}