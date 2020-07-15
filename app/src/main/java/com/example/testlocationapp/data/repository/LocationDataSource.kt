package com.example.testlocationapp.data.repository

import com.google.gson.JsonObject

interface LocationDataSource {

    fun retrieveLocations(callback: OperationCallback<JsonObject?>)
    fun cancel()
    fun cancelJobs()
    fun addShop(shopId: String?)
}