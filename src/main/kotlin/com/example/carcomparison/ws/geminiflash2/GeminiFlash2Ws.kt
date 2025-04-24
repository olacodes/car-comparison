package com.example.carcomparison.ws.geminiflash2

//import com.example.carcomparison.dto.SystemInstruction
import com.example.carcomparison.service.SystemInstruction
import com.example.carcomparison.dto.VehicleClassification

interface GeminiFlash2Ws {
    fun classifyVehicleImages(systemInstruction: SystemInstruction, vehicleImages: List<String>): VehicleClassification?
}