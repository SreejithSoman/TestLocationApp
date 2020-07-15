package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Features (

    @SerializedName("type") val type : String,
    @SerializedName("description") val description : String
) : Serializable