package org.gpo.bikesmap.retrofit.model

import com.google.gson.annotations.SerializedName

class StationInfoResponse (
    @SerializedName("data")
    var data: StationInfoResponseData
)