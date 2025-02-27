package com.example.scannerapp.view.landing

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.view.landing.model.QrOptionItem

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QRgenerator : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    var tmplist= arrayListOf(QrOptionItem(title = "Text"),QrOptionItem(title = "Web"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_q_rgenerator, container, false)


        //initialize ui
        var recyclerView=view.findViewById<RecyclerView>(R.id.generatedOptionList)
        recyclerView.layoutManager = GridLayoutManager(context,3)
        recyclerView.adapter=QrGeneratorOptionList(tmplist)
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QRgenerator().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}