package com.example.testlocationapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pickup (

    @SerializedName("feature") val feature : String,
    @SerializedName("id_pickup_location") val id_pickup_location : Int,
    @SerializedName("id_country") val id_country : Int,
    @SerializedName("id_state") val id_state : Int,
    @SerializedName("id_carrier") val id_carrier : Int,
    @SerializedName("company") val company : String,
    @SerializedName("nps_link") val nps_link : String,
    @SerializedName("alias") val alias : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("district") val district : String,
    @SerializedName("city") val city : String,
    @SerializedName("postcode") val postcode : Int,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double,
    @SerializedName("phone") val phone : String,
    @SerializedName("nearest_bts") val nearest_bts : String,
    @SerializedName("notable_area") val notable_area : String,
    @SerializedName("hours1") val hours1 : String,
    @SerializedName("hours2") val hours2 : String,
    @SerializedName("hours3") val hours3 : String,
    @SerializedName("description") val description : String,
    @SerializedName("is_featured") val is_featured : Boolean,
    @SerializedName("subtype") val subtype : String,
    @SerializedName("store_image_path") val store_image_path : String,
    @SerializedName("floormap_image_path") val floormap_image_path : String,
    @SerializedName("active") val active : Boolean,
    @SerializedName("floor_number") val floor_number : String,
    @SerializedName("status") val status : String,
    @SerializedName("id_zone") val id_zone : Int,
    @SerializedName("features") val features : List<Features>,
    @SerializedName("is_new_location") val is_new_location : Boolean,
    @SerializedName("type") val type : String,
    @SerializedName("hours") val hours : List<String>,
    @SerializedName("images") val images : Images,
    @SerializedName("is_default_location") val is_default_location : Boolean
) : Serializable