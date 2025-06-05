package com.example.scannerapp.view.landing.QrGenerator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.core.base.SimpleFragment
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.view.landing.QrGenerator.adapter.QrGeneratorOptionAdapter
import com.example.scannerapp.view.landing.QrGenerator.model.QrOptionItem


class QRgenerator : SimpleFragment() {

    var tmplist= arrayListOf(
        QrOptionItem(title = "Text", image = R.drawable.ic_text, qrOptionType = QrCodeType.TEXT),
        QrOptionItem(title = "Phone", image = R.drawable.ic_phone, qrOptionType = QrCodeType.PHONE),
        QrOptionItem(title = "SMS", image = R.drawable.ic_sms, qrOptionType = QrCodeType.SMS),
        QrOptionItem(title = "Email", image = R.drawable.ic_email, qrOptionType = QrCodeType.EMAIL),
        QrOptionItem(title = "Contact", image = R.drawable.ic_contact, qrOptionType = QrCodeType.CONTACT),
    )
    override fun getLayout(): Int {
        return R.layout.fragment_q_rgenerator
    }

    @SuppressLint("MissingInflatedId")
    override fun init() {

        //initialize ui
        val recyclerView=rootView.findViewById<RecyclerView>(R.id.generatedOptionList)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.adapter= QrGeneratorOptionAdapter(tmplist)
    }

}