package com.example.scannerapp.view.landing.history.scanHistory

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scannerapp.R
import com.example.scannerapp.core.base.BaseFragment
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.view.landing.history.adapter.ScanHistoryListAdapter
import com.example.scannerapp.view.landing.history.viewmodel.ScanHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanHistory : BaseFragment<ScanHistoryViewModel>(ScanHistoryViewModel::class.java) {

    private lateinit var  recyclerView:RecyclerView
    private lateinit var adapter: ScanHistoryListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: LinearLayout

    override fun getLayout(): Int {
       return R.layout.fragment_scan_history
    }

    override fun init() {


        var historyList= mutableListOf<QrCode>()
        recyclerView=rootView.findViewById<RecyclerView>(R.id.qrScannerList)
        progressBar=rootView.findViewById<ProgressBar>(R.id.progressBar)
        emptyView=rootView.findViewById<LinearLayout>(R.id.emptyView)
        recyclerView.layoutManager=LinearLayoutManager(context)
        adapter = ScanHistoryListAdapter(historyList, requireContext(),viewModel)
        recyclerView.adapter = adapter

        recyclerView.visibility=View.GONE
        emptyView.visibility=View.GONE

        progressBar.visibility=View.VISIBLE

        setLiveListener()
    }

    override fun viewModel() {
        viewModel.getAllScannedQrcode()
    }

    @SuppressLint("CheckResult")
    private fun setLiveListener() {
        viewModel.scannedList.subscribe { value ->


            var filterData=value
            if(filterData.size<=0){
                progressBar.visibility=View.GONE;
                recyclerView.visibility=View.GONE;
                emptyView.visibility=View.VISIBLE;
            }else{
                progressBar.visibility=View.GONE
                emptyView.visibility=View.GONE;
                recyclerView.visibility=View.VISIBLE;
                adapter.updateData(filterData)
            }

        }
    }

}