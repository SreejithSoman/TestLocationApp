package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Images (

    @SerializedName("store") val store : Store,
    @SerializedName("floormap") val floormap : Floormap
) : Serializable