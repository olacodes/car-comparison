package com.example.carcomparison.controller

import com.example.carcomparison.dto.VehicleClassificationResponse
import com.example.carcomparison.service.VehicleClassificationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vehicle-classification")
class VehicleClassificationController(private val vehicleClassificationService: VehicleClassificationService) {

    @GetMapping("/{vehicleListingId}")
    fun getVehicleClassification(@PathVariable vehicleListingId: String): VehicleClassificationResponse? {
        return vehicleClassificationService.getVehicleClassification(vehicleListingId)
    }
}
