package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Floormap (

    @SerializedName("main") val main : String,
    @SerializedName("zoomed") val zoomed : String
) : Serializable