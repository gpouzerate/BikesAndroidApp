package org.gpo.bikesmap.retrofit.model

import com.google.gson.annotations.SerializedName

class StationStatusResponseDataStation(_id: String?, _numDocksAvailable: Int, _numBikesAvailable: Int) {
    @SerializedName("station_id")
    val id: String? = _id
    @SerializedName("num_docks_available")
    val numDocksAvailable = _numDocksAvailable
    @SerializedName("num_bikes_available")
    val numBikesAvailable = _numBikesAvailable
}