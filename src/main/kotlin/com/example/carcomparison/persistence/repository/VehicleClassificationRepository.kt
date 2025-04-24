package com.example.carcomparison.persistence.repository

import com.example.carcomparison.persistence.entity.VehicleClassificationEntity
import org.springframework.data.repository.CrudRepository

interface VehicleClassificationRepository: CrudRepository<VehicleClassificationEntity, String> {
    fun findByVehicleListingId(vehicleListingId: String): VehicleClassificationEntity?
}