package com.example.scannerapp.core.common

interface RequestCompleteListener<T> {
    fun onSuccess(data:T)

    fun onFailure(errorMessage: String)
}