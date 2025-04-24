package com.example.carcomparison.ws.search

import com.example.carcomparison.dto.VehicleListing
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.lang.RuntimeException


private val logger = KotlinLogging.logger {}


@Service
class SearchWsImpl(private val webClient: WebClient): SearchWs {

    private val baseUrl = "https://api.uk.staging.group-mobility-trader.de/i15/search"

    override fun fetchVehicleListingById(vehicleListingId: String): VehicleListing? {
        val url = "$baseUrl/?id=$vehicleListingId"
        logger.info { "Fetching vehicle listing by id: $url" }
        return webClient
            .get()
            .uri(url)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(VehicleListing::class.java)
            .onErrorMap { e -> Exception(e) }
            .block()
    }

}