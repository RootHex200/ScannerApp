package com.example.scannerapp.view.landing.history.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scannerapp.db.AppDatabase
import com.example.scannerapp.db.QRHistoryInfo
import com.example.scannerapp.db.QRHistoryType
import com.google.android.material.search.SearchView.Behavior
import io.reactivex.subjects.BehaviorSubject

class ScanHistoryViewModel: ViewModel() {

    val historyList = BehaviorSubject.create<List<QRHistoryInfo>>()


    fun getHistory(context: Context){
        var response=AppDatabase.getInstance(context).qrHistoryDao().getQRHistoryList()
        historyList.onNext(response)
        Log.d("ScanHistory.LiveData.value",historyList.value!!.size.toString())
    }


    fun deleteHistory(qrHistoryInfo: QRHistoryInfo,context: Context){
       var filterdata= historyList.value.let {
            it!!.filter { it.uid!=qrHistoryInfo.uid }
        }

        historyList.onNext(filterdata)
        AppDatabase.getInstance(context).qrHistoryDao().delete(qrHistoryInfo)

    }
}