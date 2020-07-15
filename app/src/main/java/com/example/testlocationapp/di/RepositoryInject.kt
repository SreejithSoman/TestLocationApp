package com.example.testlocationapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.testlocationapp.data.repository.LocationDataSource
import com.example.testlocationapp.data.repository.LocationRepository
import com.example.testlocationapp.viewmodel.ViewModelFactory

object RepositoryInject {

    private val locationDataSource: LocationDataSource = LocationRepository()
    private val locationViewModelFactory = ViewModelFactory(locationDataSource)

    fun providerRepository(): LocationDataSource {
        return locationDataSource
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return locationViewModelFactory
    }
}