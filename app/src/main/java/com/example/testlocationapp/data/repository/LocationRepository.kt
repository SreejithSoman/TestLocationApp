package com.example.testlocationapp.data.repository

import com.example.testlocationapp.data.network.ApiClient
import com.example.testlocationapp.data.network.ApiManager
import com.example.testlocationapp.data.network.CommonApiCall
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call

const val TAG="CONSOLE"

class LocationRepository() : LocationDataSource, CommonApiCall() {

    private var call: Call<JsonObject>? = null
    private var shopId : String? = null
    var job: CompletableJob? = null

    override fun retrieveLocations(callback: OperationCallback<JsonObject?>) {

        job = Job()
        job?.let { fetchLocationJob ->
            CoroutineScope(Dispatchers.IO + fetchLocationJob).launch {
                try {

                    val locationData = ApiManager.getLocationsCall("application/json", shopId)
                    if (locationData != null) {
                        ApiManager.Coroutines.main {
                            callback.onSuccess(locationData)
                            fetchLocationJob.complete()
                        }
                    } else {
                        ApiManager.Coroutines.main {
                            if(locationData != null){
                                callback.onError(locationData)
                            } else {
                                callback.onError("Something went wrong, Please try again!")
                            }
                            fetchLocationJob.complete()
                        }
                    }

                } catch (e: Exception) {
                    ApiManager.Coroutines.main {
                        callback.onError("Something went wrong, Please try again!")
                        fetchLocationJob.complete()
                    }
                }
            }
        }


//        call = shopId?.let { ApiClient.build()?.getLocations("application/json", it) }
//        call?.enqueue(object : Callback<JsonObject> {
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                callback.onError(t.message)
//            }
//
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                response.body()?.let {
//                    if (response.isSuccessful) {
//                        Log.v(TAG, "data ${response.body()}")
//                        callback.onSuccess(response.body())
//                    } else {
//                        callback.onError(response.errorBody().toString())
//                    }
//                }
//            }
//        })
    }

    override fun cancel() {
//        call?.let {
//            it.cancel()
//        }
    }

    override fun cancelJobs() {
        job?.cancel()
    }

    override fun addShop(shopId: String?) {
        this.shopId = shopId
     }
}