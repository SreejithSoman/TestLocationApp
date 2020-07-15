package com.example.testlocationapp.data.network

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

abstract class CommonApiCall {

    suspend fun<T: Any> apiCall(call: suspend () -> Response<T>?) : T{
        val response = call.invoke()
        if(response?.isSuccessful!!){
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let{
                try{
                    message.append(JSONObject(it).getString("message"))
                }catch(e: JSONException){ }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw Exception(message.toString())
        }
    }
}


