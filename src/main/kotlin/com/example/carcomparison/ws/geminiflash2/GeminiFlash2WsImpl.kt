package com.example.carcomparison.ws.geminiflash2

//import com.example.carcomparison.dto.SystemInstruction
import com.example.carcomparison.dto.VehicleClassification
import com.example.carcomparison.service.SystemInstruction
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import mu.KotlinLogging
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@Service
class GeminiFlash2WsImpl(private val webClient: WebClient, private val objectMapper: ObjectMapper): GeminiFlash2Ws {
    private val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$GEMINI_API_KEY"


    override fun classifyVehicleImages(systemInstruction: SystemInstruction, vehicleImages: List<String>): VehicleClassification? {
        val body = buildGeminiFlash2Request(systemInstruction, vehicleImages)
        logger.info { "Request body: $body" }

        return webClient.post()
            .uri(url)
            .header("Content-Type", "application/json")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(GeminiResponse::class.java) // Step 1: Parse Gemini response
            .flatMap { geminiResponse ->
                val textJson = geminiResponse.candidates.firstOrNull()
                    ?.content?.parts?.firstOrNull()?.text
                    ?.replace("```json", "") // Remove code block markers
                    ?.replace("```", "")
                    ?.trim()
                logger.info { "Extracted JSON: $textJson" }
                if (textJson.isNullOrBlank()) {
                    logger.error("Failed to extract JSON from Gemini response")
                    Mono.empty()
                } else {
                    try {
                        val classification = objectMapper.readValue(textJson, VehicleClassification::class.java)
                        logger.info { "Vehicle classification: $classification" }
                        Mono.just(classification) // Step 2: Convert extracted JSON into VehicleClassification
                    } catch (e: Exception) {
                        logger.error("Failed to parse VehicleClassification JSON: $textJson", e)
                        Mono.empty()
                    }
                }
            }
            .block() // Step 3: Block to get the result synchronously
    }


    fun buildGeminiFlash2Request(systemInstruction: SystemInstruction, vehicleImages: List<String>): String {
        return """
        {
            "system_instruction": {
                "persona": "${systemInstruction.persona}",
                "instructions": ${systemInstruction.instructions.map { "\"$it\"" }},
                "categories": ${systemInstruction.categories.map {
            """{
                        "name": "${it.name}",
                        "criteria": ${it.criteria.map { c -> "\"$c\"" }}
                    }"""
        }},
                "output_format": {
                    "description": "${systemInstruction.output_format.description}",
                    "example": ${systemInstruction.output_format.example.entries.joinToString(",") { "\"${it.key}\": \"${it.value}\"" }}
                }
            },
            "contents": [{
                "parts": ${vehicleImages.joinToString(",") { "\"$it\"" }}
            }],
            "generationConfig": {
                "response_mime_type": "application/json"
            }
        }
    """.trimIndent()
    }

    companion object {
        private const val GEMINI_API_KEY = ""
    }
}