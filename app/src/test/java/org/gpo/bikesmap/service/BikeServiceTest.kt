package org.gpo.bikesmap.service

import android.accounts.NetworkErrorException
import org.gpo.bikesmap.retrofit.BikeInterface
import org.gpo.bikesmap.retrofit.model.*
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BikeServiceTest {

    @Test
    fun getStationsData_OK() {
        val mockedApiInterface = Mockito.mock<BikeInterface>(BikeInterface::class.java)

        mockStationsInfo(
            mockedApiInterface,
            true,
            StationInfoResponseDataStation("1", "broadway", 59.91, 10.71),
            StationInfoResponseDataStation("2", "park avenue", 59.92, 10.72)
        )
        mockStationsStatus(
            mockedApiInterface,
            true,
            StationStatusResponseDataStation("1", 5, 10),
            StationStatusResponseDataStation("2", 7, 14)
        )

        val bikeService = BikeService(mockedApiInterface)
        bikeService.getStationsData(
            resultCb = {result -> let {
                val expected1 = StationData("1", "broadway", 59.91, 10.71, 5, 10)
                val expected2 = StationData("2", "park avenue", 59.92, 10.72, 7, 14)
                Assert.assertArrayEquals(arrayOf(expected1, expected2), result.toArray())
            }
            },
            errorCb = {Assert.fail() })
    }

    @Test
    fun getStationsData_MismatchInfoStatus() {
        val mockedApiInterface = Mockito.mock<BikeInterface>(BikeInterface::class.java)

        mockStationsInfo(
            mockedApiInterface,
            true,
            StationInfoResponseDataStation("1", "broadway", 59.91, 10.71),
            StationInfoResponseDataStation("2", "park avenue", 59.92, 10.72)
        )
        mockStationsStatus(
            mockedApiInterface,
            true,
            StationStatusResponseDataStation("1", 5, 10),
            StationStatusResponseDataStation("3", 7, 14)
        )

        val bikeService = BikeService(mockedApiInterface)
        bikeService.getStationsData(
            resultCb = {result -> let {
                val expected1 = StationData("1", "broadway", 59.91, 10.71, 5, 10)
                val expected2 = StationData("2", "park avenue", null, null, -1, -1)
                Assert.assertArrayEquals(arrayOf(expected1, expected2), result.toArray())
            }
            },
            errorCb = { Assert.fail() })
    }

    @Test
    fun getStationsData_MissingStatus() {
        val mockedApiInterface = Mockito.mock<BikeInterface>(BikeInterface::class.java)

        mockStationsInfo(
            mockedApiInterface,
            true,
            StationInfoResponseDataStation("1", "broadway", 59.91, 10.71),
            StationInfoResponseDataStation("2", "park avenue", 59.92, 10.72)
        )
        mockStationsStatus(
            mockedApiInterface,
            true,
            StationStatusResponseDataStation("1", 5, 10)
        )

        val bikeService = BikeService(mockedApiInterface)
        bikeService.getStationsData(
            resultCb = {result -> let {
                val expected1 = StationData("1", "broadway", 59.91, 10.71, 5, 10)
                val expected2 = StationData("2", "park avenue", null, null, -1, -1)
                Assert.assertArrayEquals(arrayOf(expected1, expected2), result.toArray())
            }
            },
            errorCb = { Assert.fail() })
    }

    @Test
    fun getStationsData_NetworkError() {
        val mockedApiInterface = Mockito.mock<BikeInterface>(BikeInterface::class.java)

        mockStationsInfo(
            mockedApiInterface,
            false,
            StationInfoResponseDataStation("1", "broadway", 59.91, 10.71),
            StationInfoResponseDataStation("2", "park avenue", 59.92, 10.72)
        )
        mockStationsStatus(
            mockedApiInterface,
            true,
            StationStatusResponseDataStation("1", 5, 10)
        )

        val bikeService = BikeService(mockedApiInterface)
        bikeService.getStationsData(
            resultCb = {Assert.fail()},
            errorCb = {reason -> Assert.assertNotNull(reason)})
    }

    private fun mockStationsInfo(mockedApiInterface: BikeInterface, success: Boolean = true, vararg stationsInfo: StationInfoResponseDataStation){
        @Suppress("UNCHECKED_CAST")
        val call: Call<StationInfoResponse> = Mockito.mock(Call::class.java) as Call<StationInfoResponse>
        Mockito.`when`(mockedApiInterface.stationInformation()).thenReturn(call)

        Mockito.`when`(call.enqueue(Mockito.any()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                val callback: Callback<StationInfoResponse> = invocation?.getArgument(0, Callback::class.java) as Callback<StationInfoResponse>
                if (success) {
                    val stations: ArrayList<StationInfoResponseDataStation> =arrayListOf(*stationsInfo)
                    val resp = StationInfoResponse(StationInfoResponseData(stations))
                    callback.onResponse(call, Response.success(resp))
                } else {
                    callback.onFailure(call, NetworkErrorException("Network error"))
                }
                null
            }
    }

    private fun mockStationsStatus(mockedApiInterface: BikeInterface, success: Boolean = true, vararg stationsStatus: StationStatusResponseDataStation){
        @Suppress("UNCHECKED_CAST")
        val call: Call<StationStatusResponse> = Mockito.mock(Call::class.java) as Call<StationStatusResponse>
        Mockito.`when`(mockedApiInterface.stationStatus()).thenReturn(call)

        Mockito.`when`(call.enqueue(Mockito.any()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                val callback: Callback<StationStatusResponse> = invocation?.getArgument(0, Callback::class.java) as Callback<StationStatusResponse>
                if (success) {
                    val stations: ArrayList<StationStatusResponseDataStation> =arrayListOf(*stationsStatus)
                    val resp = StationStatusResponse(StationStatusResponseData(stations))
                    callback.onResponse(call, Response.success(resp))
                } else {
                    callback.onFailure(call, NetworkErrorException("Network error"))
                }
                null
            }
    }
}