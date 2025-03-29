package com.example.scannerapp.view.landing.history.scanHistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.db.QRHistoryInfo
import com.example.scannerapp.db.QRHistoryType
import com.example.scannerapp.view.landing.history.adapter.ScanHistoryListAdapter
import com.example.scannerapp.view.landing.history.viewmodel.ScanHistoryViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: ScanHistoryViewModel by viewModels()
    private lateinit var   recyclerView:RecyclerView
    private lateinit var adapter: ScanHistoryListAdapter
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
        // Inflate the layout for this fragment

        context?.let { viewModel.getHistory(it) }
        var view:View=inflater.inflate(R.layout.fragment_scan_history, container, false)
        var historyList= mutableListOf<QRHistoryInfo>()
        recyclerView=view.findViewById<RecyclerView>(R.id.qrScannerList)
        recyclerView.layoutManager=LinearLayoutManager(context)
        adapter = ScanHistoryListAdapter(historyList, requireContext(),viewModel)
        recyclerView.adapter = adapter

        setLiveListener()

        return view
    }

    @SuppressLint("CheckResult")
    private fun setLiveListener() {
        viewModel.historyList.subscribe { value ->


            var filterData=value.filter { it.historyType==QRHistoryType.SCAN_HISTORY }
            adapter.updateData(filterData)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}