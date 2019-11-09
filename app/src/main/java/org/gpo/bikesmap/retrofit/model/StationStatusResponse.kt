package org.gpo.bikesmap.retrofit.model

import com.google.gson.annotations.SerializedName

class StationStatusResponse (
    @SerializedName("data")
    var data: StationStatusResponseData
)