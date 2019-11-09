package org.gpo.bikesmap.service

data class StationData(val id: String, var name: String?, var lat: Double?, var lon: Double?, var numDocksAvailable: Int, var numBikesAvailable: Int)