package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Store (

    @SerializedName("primary") val primary : Primary,
    @SerializedName("secondary") val secondary : String
) : Serializable