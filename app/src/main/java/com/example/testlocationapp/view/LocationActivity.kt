package com.example.testlocationapp.view

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testlocationapp.R
import com.example.testlocationapp.data.model.Pickup
import com.example.testlocationapp.di.RepositoryInject
import com.example.testlocationapp.utils.GPSTracker
import com.example.testlocationapp.utils.LocationComparator
import com.example.testlocationapp.view.dialogs.SettingsAlertDialog
import com.example.testlocationapp.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.layout_error.*
import java.util.*
import kotlin.collections.ArrayList


class LocationActivity : AppCompatActivity(), SettingsAlertDialog.DialogListener {

    private lateinit var viewModel: LocationViewModel
    private lateinit var adapter: LocationAdapter
    private lateinit var gpsTracker : GPSTracker
    private var locationList: MutableList<Pickup> = ArrayList()
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var alertSettings: AlertDialog? = null

    companion object {
        const val TAG = "CONSOLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupViewModel()
        setupUI()
        eventListener()
    }

    private fun eventListener() {
        imageSearch.setOnClickListener(View.OnClickListener {
            checkLocationPermission()
        })
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLatLon()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ), 10
                    )
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ), 10
                    )
                }
            }
        } else {
            getLatLon()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLatLon()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        (this as Activity?)!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    val fm: FragmentManager = supportFragmentManager
                    val alertDialog: SettingsAlertDialog? = SettingsAlertDialog.newInstance(this,"Permission Required","You have to Allow permission to access user location" )
                    alertDialog?.show(fm, "settings_alert")

//                    val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
//                    alertDialog.setTitle("Permission Required")
//                    alertDialog.setCancelable(false)
//                    alertDialog.setMessage("You have to Allow permission to access user location")
//                    alertDialog.setPositiveButton("Settings",
//                        DialogInterface.OnClickListener { dialog, which ->
//                            val i = Intent(
//                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
//                                    "package",
//                                    this.packageName, null
//                                )
//                            )
//                            startActivityForResult(i, 1001)
//                            dialog.dismiss()
//                            dialog.cancel()
//                        })
//                    alertDialog.show();
//                    alertSettings = alertDialog.create()
//                    alertSettings!!.show()
                }
            }
        }
    }


    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        when (requestCode) {
            1001 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getLatLon()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ), 10
                        )
                    } else {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 10
                        )
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun getLatLon() {
        gpsTracker = GPSTracker(this)
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude()
            longitude = gpsTracker.getLongitude()
        }
        if(latitude != 0.0 || longitude != 0.0){
            if(!locationList.isNullOrEmpty()){
                Collections.sort(locationList, LocationComparator(latitude, longitude))
                adapter.setData(locationList)
            }
        }
    }

    private fun setupUI() {
        viewModel.locationData.value.let {
            if(!it.isNullOrEmpty()){
                repeat(it.size){it1->
                    if(it[it1].active
                        && (!it[it1].alias.isBlank()
                                || !it[it1].address1.isBlank()
                                || !it[it1].city.isBlank())){
                        locationList.add(it[it1])
                    }
                }
            }
        }

        if(latitude != 0.0 || longitude != 0.0){
            if(!locationList.isNullOrEmpty()){
                Collections.sort(locationList, LocationComparator(latitude, longitude))
            }
        }
        adapter = LocationAdapter(this, locationList)
        rvLocationList?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvLocationList?.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, RepositoryInject.provideViewModelFactory()).get(LocationViewModel::class.java)
        viewModel.locationData.observe(this, renderLocations)

        viewModel.isViewLoading.observe(this,isViewLoadingObserver)
        viewModel.onMessageError.observe(this,onMessageErrorObserver)
        viewModel.isEmptyList.observe(this,emptyListObserver)
    }

    private val renderLocations= Observer<List<Pickup>> {
        Log.v(TAG, "data updated $it")
        layoutError.visibility=View.GONE
        layoutEmpty.visibility=View.GONE
        if(!it.isNullOrEmpty()) {
            locationList = ArrayList()
            repeat(it.size) { it1 ->
                if (it[it1].active
                    && (!it[it1].alias.isBlank()
                            || !it[it1].address1.isBlank()
                            || !it[it1].city.isBlank())
                ) {
                    locationList.add(it[it1])
                }
            }
            if (latitude != 0.0 || longitude != 0.0) {
                Collections.sort(locationList, LocationComparator(latitude, longitude))
            }
        } else {
            locationList = it.toMutableList()
        }
        adapter.setData(locationList)
    }

    private val isViewLoadingObserver= Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility = if(it)View.VISIBLE else View.GONE
        progressBar.visibility= visibility
    }

    private val onMessageErrorObserver= Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        layoutError.visibility=View.VISIBLE
        layoutEmpty.visibility=View.GONE
        textViewError.text= "Error $it"
    }

    private val emptyListObserver= Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility=View.VISIBLE
        layoutError.visibility=View.GONE
    }

    override fun onStart() {
        super.onStart()
        getLatLon()
    }

    override fun onStop() {
        gpsTracker.stopUsingGPS()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLocations("1")
    }

    override fun onDestroy() {
        viewModel.cancelJobs()
//        viewModel.cancel()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClickSettings() {
        val i = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                "package",
                this.packageName, null
            )
        )
        startActivityForResult(i, 1001)
    }

    override fun onBackPressed() {
        if(alertSettings != null) {
            alertSettings!!.dismiss()
            alertSettings!!.cancel()
        }
        super.onBackPressed()
    }
}
