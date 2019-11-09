package org.gpo.bikesmap.retrofit

import org.gpo.bikesmap.retrofit.model.StationInfoResponse
import org.gpo.bikesmap.retrofit.model.StationStatusResponse
import retrofit2.Call
import retrofit2.http.GET

interface BikeInterface {
    @GET("station_information.json")
    fun stationInformation(): Call<StationInfoResponse>

    @GET("station_status.json")
    fun stationStatus(): Call<StationStatusResponse>
}