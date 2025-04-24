package com.example.carcomparison.service

//import com.example.carcomparison.dto.SystemInstruction
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.ObjectMapper

data class SystemInstruction(
    val persona: String,
    val instructions: List<String>,
    val categories: List<Category>,
    val output_format: OutputFormat
)

data class Category(
    val name: String,
    val criteria: List<String>
)

data class OutputFormat(
    val description: String,
    val example: Map<String, String>
)

@Service
class SystemInstructionLoader(private val resourceLoader: ResourceLoader, private val objectMapper: ObjectMapper) {

    fun loadSystemInstruction(): SystemInstruction {
        val resource = resourceLoader.getResource("classpath:gemini_system_instructions.json")
        val json = resource.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
        return objectMapper.readValue(json, SystemInstruction::class.java)
    }
}

