package com.example.testlocationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testlocationapp.data.model.LocationResponse
import com.example.testlocationapp.data.model.Pickup
import com.example.testlocationapp.data.repository.LocationDataSource
import com.example.testlocationapp.data.repository.OperationCallback
import com.google.gson.Gson
import com.google.gson.JsonObject

class LocationViewModel(private val repository : LocationDataSource) : ViewModel() {

    private var locations = MutableLiveData<List<Pickup>>().apply { value = emptyList() }
    var locationData: LiveData<List<Pickup>> = locations

    private var _isViewLoading = MutableLiveData<Boolean>()
    var isViewLoading:LiveData<Boolean> = _isViewLoading

    private var _onMessageError = MutableLiveData<Any>()
    var onMessageError:LiveData<Any> = _onMessageError

    private var _isEmptyList = MutableLiveData<Boolean>()
    var isEmptyList:LiveData<Boolean> = _isEmptyList

    fun loadLocations(shopId: String?) {
        _isViewLoading.postValue(true)
        repository.addShop(shopId)
        repository.retrieveLocations(object:
            OperationCallback<JsonObject?> {
            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue( error)
            }

            override fun onSuccess(data: JsonObject?) {
                _isViewLoading.postValue(false)
                if(data != null) {
                    val locationModels: LocationResponse? = Gson().fromJson(data, LocationResponse::class.java)
                    val locationModel: List<Pickup>? = locationModels?.pickup
                    if (locationModel != null) {
                        if (locationModel.isEmpty()) {
                            _isEmptyList.postValue(true)
                        } else {
                            locations.value = locationModel
                        }
                    } else _isEmptyList.postValue(true)
                } else _isEmptyList.postValue(true)
            }
        })
    }

    fun cancelJobs(){
        repository.cancelJobs()
    }

    fun cancel() {
        repository.cancel()
    }
}