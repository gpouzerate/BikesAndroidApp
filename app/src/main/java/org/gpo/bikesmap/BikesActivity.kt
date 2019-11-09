package org.gpo.bikesmap

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.gpo.bikesmap.retrofit.BikeInterfaceFactory
import org.gpo.bikesmap.service.BikeService
import org.gpo.bikesmap.service.StationData

class BikesActivity : AppCompatActivity(), OnMapReadyCallback {
    private val osloLatLng = LatLng(59.9139, 10.7522)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        val clusterManager = ClusterManager<StationClusterItem>(applicationContext, gMap)
        val renderer = CustomClusterRenderer(this, gMap, clusterManager)
        clusterManager.renderer = renderer
        gMap.setOnCameraIdleListener(clusterManager)

        val bikesFacade = BikeService(BikeInterfaceFactory.createBikeInterface())
        bikesFacade.getStationsData(
            resultCb = { stationsData ->
                let {
                    for (data in stationsData) {
                        clusterManager.addItem(StationClusterItem(data))
                    }
                    clusterManager.cluster()
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(osloLatLng, 12f))
                }
            },
            errorCb = {reason ->
                Toast.makeText(this, "Could not fetch stations: $reason", Toast.LENGTH_LONG).show()
            }
        )
    }

    inner class CustomClusterRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<StationClusterItem>) : DefaultClusterRenderer<StationClusterItem>(context, map, clusterManager) {
        override fun onBeforeClusterItemRendered(item: StationClusterItem, markerOptions: MarkerOptions?) {
            val markerColor: Float =  if(item.stationData.numBikesAvailable > 0 && item.stationData.numDocksAvailable > 0) HUE_GREEN else HUE_ORANGE
            val markerDescriptor: BitmapDescriptor = defaultMarker(markerColor)
            markerOptions?.icon(markerDescriptor)
        }
    }

    inner class StationClusterItem(val stationData: StationData) : ClusterItem {
        override fun getPosition(): LatLng = LatLng(stationData.lat?:0.0, stationData.lon?: 0.0)
        override fun getTitle(): String = stationData.name?:"Undefined"
        override fun getSnippet(): String = "Bikes: ${stationData.numBikesAvailable}, Docks: ${stationData.numDocksAvailable}"
    }
}
