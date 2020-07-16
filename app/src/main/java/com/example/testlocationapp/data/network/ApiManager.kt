package com.example.testlocationapp.data.network

import com.example.testlocationapp.data.model.LocationResponse
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object ApiManager : CommonApiCall() {

    suspend fun getLocationsCall(headerType: String, shopId: String?): JsonObject {
        return apiCall{ shopId?.let { ApiClient.build()?.getLocations(headerType, it) } }
    }

    object ResponseType {
        @SerializedName("status")
        @Expose
        var status: Boolean? = null
        @SerializedName("message")
        @Expose
        var message: String? = null
        @SerializedName("data")
        @Expose
        var data: JsonObject? = null
    }

    object Coroutines {

        fun main(work: suspend (() -> Unit)) =
            CoroutineScope(Dispatchers.Main).launch {
                work()
            }

        fun io(work: suspend (() -> Unit)) =
            CoroutineScope(Dispatchers.IO).launch {
                work()
            }

        suspend fun with(work: suspend (() -> Unit)) =
            withContext(Dispatchers.Main) {
                work()
            }
    }

    interface ServicesApiInterface {

        @GET("v3/pickup-locations/")
        suspend fun getLocations(
            @Header("Content-Type") headType: String,
            @Query("filter[shop_id]") id: String
        ): Response<JsonObject>
    }
}