package com.example.scannerapp.view.landing.history.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.core.db.AppDatabase
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.repositories.QrCodeRepositories
import com.google.android.material.search.SearchView.Behavior
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ScanHistoryViewModel @Inject constructor(
    private val repository: QrCodeRepositories
): BaseViewModel() {

    val scannedList = BehaviorSubject.create<List<QrCode>>()
    val createList=BehaviorSubject.create<List<QrCode>>()

    fun getAllScannedQrcode(){
        var response= repository.getAllScannedQrCode()
        scannedList.onNext(response)
        Log.d("ScanHistory.LiveData.value",scannedList.value!!.size.toString())
    }
    fun getAllCreatedQrcode(){
        var response= repository.getAllCreatedQrCode()
        createList.onNext(response)
        Log.d("ScanHistory.LiveData.value",createList.value!!.size.toString())
    }

    fun deleteHistory(qrCode: QrCode){
       repository.deleteQrCodeData(qrCode)
    }

}