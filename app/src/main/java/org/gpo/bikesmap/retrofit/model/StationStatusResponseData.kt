package org.gpo.bikesmap.retrofit.model

import com.google.gson.annotations.SerializedName

class StationStatusResponseData (_stations: ArrayList<StationStatusResponseDataStation>) {
    @SerializedName("stations")
    var stations = _stations
}