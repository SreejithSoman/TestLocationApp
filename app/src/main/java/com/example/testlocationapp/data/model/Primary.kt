package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Primary (

    @SerializedName("landscape") val landscape : String,
    @SerializedName("portrait") val portrait : String
) : Serializable