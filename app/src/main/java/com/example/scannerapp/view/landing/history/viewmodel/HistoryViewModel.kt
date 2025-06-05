package com.example.scannerapp.view.landing.history.viewmodel

import android.util.Log
import com.example.scannerapp.core.base.BaseViewModel
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.repositories.QrCodeRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.BehaviorSubject
import jakarta.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: QrCodeRepositories
): BaseViewModel() {

    var scannedList = BehaviorSubject.create<List<QrCode>>()
    var createList= BehaviorSubject.create<List<QrCode>>()

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

    fun deleteScanQrCode(qrCode: QrCode){
       repository.deleteQrCodeData(qrCode)
        Log.d("deleteQrCode",scannedList.value?.size.toString())
        val scanEditList=scannedList.value?.filter {
            it.id!=qrCode.id
        }
        Log.d("deleteQrCode",scanEditList?.size.toString())
        scannedList.onNext(scanEditList!!)

    }

    fun deleteCreatedQrCode(qrcode: QrCode){
        Log.d("deleteQrCode",createList.value?.size.toString())
        repository.deleteQrCodeData(qrcode)
       val createEditList= createList.value?.filter {
            it.id!=qrcode.id
        }
        Log.d("deleteQrCode",createEditList?.size.toString())
        createList.onNext(createEditList!!)
    }


}