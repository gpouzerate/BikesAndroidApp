package org.gpo.bikesmap.retrofit.model

import com.google.gson.annotations.SerializedName

class StationInfoResponseData(_stations: ArrayList<StationInfoResponseDataStation>) {
    @SerializedName("stations")
    var stations = _stations
}