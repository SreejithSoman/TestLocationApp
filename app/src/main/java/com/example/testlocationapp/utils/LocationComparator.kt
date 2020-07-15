package com.example.testlocationapp.utils

import android.location.Location
import com.example.testlocationapp.data.model.Pickup

class LocationComparator(private var latitude: Double, private var longitude: Double) : Comparator<Pickup?> {


    override fun compare(o1: Pickup?, o2: Pickup?): Int {
        val lat1: Double = o1?.latitude ?: 0.0
        val lon1: Double = o1?.longitude ?: 0.0
        val lat2: Double = o2?.latitude ?: 0.0
        val lon2: Double = o2?.longitude ?: 0.0

        val distanceToPlace1: Double = distance(latitude, longitude, lat1, lon1)
        val distanceToPlace2: Double = distance(latitude, longitude, lat2, lon2)
        return (distanceToPlace1 - distanceToPlace2).toInt()
    }

    private fun distance(
        fromLat: Double,
        fromLon: Double,
        toLat: Double,
        toLon: Double
    ): Double {

        val source = Location("Start Point")
        source.latitude = fromLat
        source.longitude = fromLon

        val destination = Location("End Point")
        destination.latitude = toLat
        destination.longitude = toLon

        return source.distanceTo(destination).toDouble()
    }
}

