package com.example.carcomparison.ws.geminiflash2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiResponse(
    val candidates: List<Candidate>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Candidate(
    val content: Content
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Content(
    val parts: List<Part>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Part(
    val text: String
)