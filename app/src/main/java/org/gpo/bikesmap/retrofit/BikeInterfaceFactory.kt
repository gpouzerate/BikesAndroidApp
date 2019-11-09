package org.gpo.bikesmap.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val serviceBaseUrl = "https://gbfs.urbansharing.com/oslobysykkel.no/"

class BikeInterfaceFactory {
    companion object {
        fun createBikeInterface(): BikeInterface {
            val client = OkHttpClient().newBuilder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(serviceBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(BikeInterface::class.java)
        }
    }
}