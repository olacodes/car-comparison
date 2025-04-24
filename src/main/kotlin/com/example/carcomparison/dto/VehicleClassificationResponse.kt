package com.example.carcomparison.dto

data class VehicleClassificationResponse(
    val vehicleListingId: String,
    val vehicleClassification: VehicleClassification,
)

data class VehicleClassification(
    val priceAndIncentives: String? = null,
    val overviewAndAccessibility: String? = null,
    val engineAndPerformance: String? = null,
    val bodyAndInterior: String? = null,
    val emissionsAndRange: String? = null,
)



