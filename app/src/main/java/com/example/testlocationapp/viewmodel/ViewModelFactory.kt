package com.example.testlocationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testlocationapp.data.repository.LocationDataSource


class ViewModelFactory(private val repository: LocationDataSource): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationViewModel(repository) as T
    }
}