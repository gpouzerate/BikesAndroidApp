package org.gpo.bikesmap.retrofit.model

import com.google.gson.annotations.SerializedName

class StationInfoResponseDataStation(_id: String?, _name: String?, _lat: Double?, _lon: Double?) {
    @SerializedName("station_id")
    val id: String? = _id
    @SerializedName("name")
    val name: String? = _name
    @SerializedName("lat")
    val lat: Double? = _lat
    @SerializedName("lon")
    val lon: Double? = _lon
}