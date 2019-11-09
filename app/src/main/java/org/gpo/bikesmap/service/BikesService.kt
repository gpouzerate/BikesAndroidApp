package org.gpo.bikesmap.service

import org.gpo.bikesmap.retrofit.BikeInterface
import org.gpo.bikesmap.retrofit.model.StationInfoResponse
import org.gpo.bikesmap.retrofit.model.StationInfoResponseDataStation
import org.gpo.bikesmap.retrofit.model.StationStatusResponse
import org.gpo.bikesmap.retrofit.model.StationStatusResponseDataStation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BikeService(private val bikeItf: BikeInterface) {

    fun getStationsData(resultCb: (stationsData: ArrayList<StationData>) -> Unit, errorCb: (reason: String) -> Unit) {
        val stationsData = ArrayList<StationData>()
        getStationsInfo(successCb = { stationsInfo ->
            getStationsStatus(successCb = { stationsStatus ->
                for (info in stationsInfo) {
                    val status = stationsStatus[info.key]
                    if (status != null)
                        stationsData.add(StationData(info.key, info.value.name, info.value.lat, info.value.lon, status.numDocksAvailable, status.numBikesAvailable))
                    else
                        stationsData.add(StationData(info.key, info.value.name, null, null, -1, -1))
                }
                resultCb(stationsData)
            }, errorCb = {reason -> errorCb("Error getting stations status: $reason")})
        }, errorCb = {reason -> errorCb("Error getting stations info: $reason")}
        )
    }

    private fun getStationsInfo(successCb: (stations: MutableMap<String, StationInfoResponseDataStation>) -> Unit, errorCb: (reason: String) -> Unit) {
        bikeItf.stationInformation().enqueue(object : Callback<StationInfoResponse> {
            override fun onResponse(systemInfo: Call<StationInfoResponse>, response: Response<StationInfoResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val stationsInfo: MutableMap<String, StationInfoResponseDataStation> = hashMapOf()
                        for (station : StationInfoResponseDataStation in body.data.stations) {
                            if (station.id != null)
                                stationsInfo[station.id] = station
                        }
                        successCb(stationsInfo)
                    } else {
                        errorCb("Response body was null.")
                    }
                } else {
                    errorCb("Response was not successful.")
                }
            }

            override fun onFailure(call: Call<StationInfoResponse>, t: Throwable) = errorCb(t.localizedMessage?:"Exception")
        })
    }

    private fun getStationsStatus(successCb: (stations: MutableMap<String, StationStatusResponseDataStation>) -> Unit, errorCb: (reason: String) -> Unit) {
        bikeItf.stationStatus().enqueue(object : Callback<StationStatusResponse> {
            override fun onResponse(stationStatus: Call<StationStatusResponse>, response: Response<StationStatusResponse>) {
                if (response.isSuccessful) {
                    val body: StationStatusResponse? = response.body();
                    if (body != null) {
                        val stationsStatus: MutableMap<String, StationStatusResponseDataStation> = hashMapOf()
                        for (station in body.data.stations) {
                            if (station.id != null)
                                stationsStatus[station.id] = station
                        }
                        successCb(stationsStatus)
                    } else {
                        errorCb("Response body was null.")
                    }
                } else {
                    errorCb("Response was not successful.")
                }
            }

            override fun onFailure(call: Call<StationStatusResponse>, t: Throwable) {
                errorCb(t.localizedMessage ?: "Exception")
            }
        })
    }
}