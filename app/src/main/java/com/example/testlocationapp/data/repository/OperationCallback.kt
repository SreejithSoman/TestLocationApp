package com.example.testlocationapp.data.repository

interface OperationCallback<T> {
    fun onSuccess(data:T?)
    fun onError(error:String?)
}