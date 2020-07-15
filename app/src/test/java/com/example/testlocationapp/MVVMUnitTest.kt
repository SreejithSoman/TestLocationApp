package com.example.testlocationapp

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.testlocationapp.data.model.*
import com.example.testlocationapp.data.repository.LocationDataSource
import com.example.testlocationapp.data.repository.OperationCallback
import com.example.testlocationapp.viewmodel.LocationViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class MVVMUnitTest {

    @Mock
    private lateinit var repository: LocationDataSource
    @Mock
    private lateinit var context: Application

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<OperationCallback<JsonObject?>>

    private lateinit var viewModel:LocationViewModel

    private lateinit var isViewLoadingObserver: Observer<Boolean>
    private lateinit var onMessageErrorObserver:Observer<Any>
    private lateinit var emptyListObserver:Observer<Boolean>
    private lateinit var onRenderLocationObserver:Observer<List<Pickup>>

    private lateinit var locationEmptyList:List<Pickup>
    private lateinit var locationList:List<Pickup>
    private lateinit var locationModel:LocationResponse
    private lateinit var jsonObject:JsonObject
    private lateinit var emptyJsonObject:JsonObject

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`<Context>(context.applicationContext).thenReturn(context)
        viewModel= LocationViewModel(repository)
        mockData()
        setupObservers()
    }

    @Test
    fun `retrieve locationData with ViewModel and Repository returns empty data`(){
        with(viewModel){
            loadLocations("1")
            isViewLoading.observeForever(isViewLoadingObserver)
            isEmptyList.observeForever(emptyListObserver)
            locationData.observeForever(onRenderLocationObserver)
        }

        verify(repository, times(1)).retrieveLocations(capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onSuccess(emptyJsonObject)

        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.isEmptyList.value == true)
        Assert.assertTrue(viewModel.locationData.value?.size==0)
    }

    @Test
    fun `retrieve locationData with ViewModel and Repository returns full data`(){
        with(viewModel){
            loadLocations("1")
            isViewLoading.observeForever(isViewLoadingObserver)
            locationData.observeForever(onRenderLocationObserver)
        }

        verify(repository, times(1)).retrieveLocations(capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onSuccess(jsonObject)

        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.locationData.value?.size==3)
    }

    @Test
    fun `retrieve locationData with ViewModel and Repository returns an error`(){
        with(viewModel){
            loadLocations("1")
            isViewLoading.observeForever(isViewLoadingObserver)
            onMessageError.observeForever(onMessageErrorObserver)
        }
        verify(repository, times(1)).retrieveLocations(capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onError("An error occurred")
        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertNotNull(viewModel.onMessageError.value)
    }

    private fun setupObservers(){
        isViewLoadingObserver= mock(Observer::class.java) as Observer<Boolean>
        onMessageErrorObserver= mock(Observer::class.java) as Observer<Any>
        emptyListObserver= mock(Observer::class.java) as Observer<Boolean>
        onRenderLocationObserver= mock(Observer::class.java)as Observer<List<Pickup>>
    }

    private fun mockData(){
        locationEmptyList= emptyList()
        val mockList:MutableList<Pickup> = mutableListOf()

        val source = Pickup("a",1,1,1,1,"a","a","a",
            "a","a","a","a",1,0.0,0.0,"a","a","a",
            "a","a","a","a",false,"a","a","a",
            false,"a","a",1, emptyList(),false,"a", emptyList(), Images(Store(Primary("a","a"),"a"), Floormap("a","a")),false)

        val source1 = Pickup("a",1,1,1,1,"a","a","a",
            "a","a","a","a",1,0.0,0.0,"a","a","a",
            "a","a","a","a",false,"a","a","a",
            false,"a","a",1, emptyList(),false,"a", emptyList(), Images(Store(Primary("a","a"),"a"), Floormap("a","a")),false)

        val source2 = Pickup("a",1,1,1,1,"a","a","a",
            "a","a","a","a",1,0.0,0.0,"a","a","a",
            "a","a","a","a",false,"a","a","a",
            false,"a","a",1, emptyList(),false,"a", emptyList(), Images(Store(Primary("a","a"),"a"), Floormap("a","a")),false)

        mockList.add(source)
        mockList.add(source1)
        mockList.add(source2)

        locationList = mockList.toList()
        locationModel = LocationResponse(3, locationList)

        val strList : String = Gson().toJson(locationModel)
        jsonObject = JsonParser.parseString(strList).asJsonObject
//        val emptyList = emptyList<Pickup>()
        val locData = LocationResponse( 0, locationEmptyList)

        val strEmptyList : String = Gson().toJson(locData)
        emptyJsonObject = JsonParser.parseString(strEmptyList).asJsonObject
    }
}