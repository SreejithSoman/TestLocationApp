package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationResponse (
    @SerializedName("number_of_new_locations") val number_of_new_locations : Int,
    @SerializedName("pickup") val pickup : List<Pickup>
) : Serializable