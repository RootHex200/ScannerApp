package com.example.scannerapp.view.landing.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.scannerapp.R
import com.example.scannerapp.R.color.secondaryColor2
import com.example.scannerapp.view.landing.history.createHistory.createHistory
import com.example.scannerapp.view.landing.history.scanHistory.ScanHistory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QRhistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class QRhistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentFragmentManager.beginTransaction().replace(
            R.id.qrHistoryframlayout,ScanHistory()
        ).commit()
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_q_rhistory, container, false)

        var scanHistory=view.findViewById<LinearLayout>(R.id.scanHistoryBtn)
        var createHistory=view.findViewById<LinearLayout>(R.id.createHistoryBtn)
       // var scanFragment=view.findViewById<FrameLayout>(R.id.qrHistoryframlayout)
        scanHistory.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.qrHistoryframlayout,ScanHistory()
            ).commit()
            scanHistory.setBackgroundResource(R.drawable.linear_color)
            createHistory.setBackgroundColor(ContextCompat.getColor(container!!.context,R.color.secondaryColor2))
        }
        createHistory.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.qrHistoryframlayout,createHistory()
            ).commit()
            createHistory.setBackgroundResource(R.drawable.linear_color)
            scanHistory.setBackgroundColor(ContextCompat.getColor(container!!.context,R.color.secondaryColor2))
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QRhistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QRhistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}