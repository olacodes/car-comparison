package com.example.carcomparison.ws.search

import com.example.carcomparison.dto.VehicleListing

interface SearchWs {
    fun fetchVehicleListingById(vehicleListingId: String): VehicleListing?
}