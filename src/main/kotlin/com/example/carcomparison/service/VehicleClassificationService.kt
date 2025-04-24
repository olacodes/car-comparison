package com.example.carcomparison.service

import com.example.carcomparison.dto.VehicleClassification
import com.example.carcomparison.dto.VehicleClassificationResponse
import com.example.carcomparison.persistence.entity.VehicleClassificationEntity
import com.example.carcomparison.persistence.repository.VehicleClassificationRepository
import com.example.carcomparison.ws.geminiflash2.GeminiFlash2Ws
import com.example.carcomparison.ws.search.SearchWs
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}


@Service
class VehicleClassificationService(
    private val geminiFlash2Ws: GeminiFlash2Ws,
    private val searchWs: SearchWs,
    private val vehicleClassificationRepository: VehicleClassificationRepository,
    private val objectMapper: ObjectMapper,
    private val systemInstructionLoader: SystemInstructionLoader
) {

    fun getVehicleClassification(vehicleListingId: String): VehicleClassificationResponse? {
        val vehicleImages = fetchVehicleImages(vehicleListingId) ?: return null
        logger.info { "Vehicle images fetched successfully: $vehicleListingId"}
        return getCachedClassification(vehicleListingId) ?: classifyAndSaveVehicle(vehicleListingId, vehicleImages)
    }

    private fun fetchVehicleImages(vehicleListingId: String): List<String>? {
        val vehicleListing = searchWs.fetchVehicleListingById(vehicleListingId)
        return vehicleListing?.content?.firstOrNull()?.images?.map { it.url }
    }

    private fun getCachedClassification(vehicleListingId: String): VehicleClassificationResponse? {
        val entity = vehicleClassificationRepository.findByVehicleListingId(vehicleListingId)
        logger.info { "Vehicle classification fetched from cache: $entity" }
        return if (entity?.isCategorised == true) {
            val classification = objectMapper.readValue(entity.categorizedResult, VehicleClassification::class.java)
            VehicleClassificationResponse(vehicleListingId, classification)
        } else null
    }

    private fun classifyAndSaveVehicle(vehicleListingId: String, vehicleImages: List<String>): VehicleClassificationResponse? {
        val systemInstruction = systemInstructionLoader.loadSystemInstruction()
        val classificationResult = geminiFlash2Ws.classifyVehicleImages(systemInstruction, vehicleImages) ?: return null
        logger.info { "Vehicle classified successfully: $vehicleListingId" }

        saveClassification(vehicleListingId, classificationResult)
        return VehicleClassificationResponse(vehicleListingId, classificationResult)
    }

    private fun saveClassification(vehicleListingId: String, classification: VehicleClassification) {
        val entity = VehicleClassificationEntity(
            vehicleListingId = vehicleListingId,
            isCategorised = true,
            categorizedResult = objectMapper.writeValueAsString(classification)
        )
        vehicleClassificationRepository.save(entity)
    }
}
