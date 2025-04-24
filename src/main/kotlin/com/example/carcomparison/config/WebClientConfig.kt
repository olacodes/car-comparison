package com.example.carcomparison.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.util.unit.DataSize.ofMegabytes
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

private const val MAX_MEMORY_IN_MB: Long = 16

@Configuration
@EnableConfigurationProperties(HttpConnectionProperties::class)
class WebClientConfig(val httpProperties: HttpConnectionProperties) {

    @Bean
    fun webClientHttpClient(): HttpClient {
        return HttpClient.create()
            .resolver(DefaultAddressResolverGroup.INSTANCE)
            .responseTimeout(Duration.ofMillis(httpProperties.connectionTimeout.toLong()))
            .followRedirect(true)
    }

    @Bean
    fun webClient(
        webClientBuilder: WebClient.Builder,
        webClientHttpClient: HttpClient,
        objectMapper: ObjectMapper,
        encoder: Jackson2JsonEncoder,
        decoder: Jackson2JsonDecoder,
    ): WebClient {
        return webClientBuilder
            .clientConnector(ReactorClientHttpConnector(webClientHttpClient))
            .exchangeStrategies(jsonExchangeStrategyHelper(objectMapper, encoder, decoder))
            .defaultHeaders { headers ->
                headers.contentType = MediaType.APPLICATION_JSON
                headers.accept = listOf(MediaType.APPLICATION_JSON)
            }
            .build()
    }

    @Bean
    fun jackson2JsonEncoder(mapper: ObjectMapper): Jackson2JsonEncoder =
        Jackson2JsonEncoder(mapper, MediaType.APPLICATION_JSON)

    @Bean
    fun jackson2JsonDecoder(mapper: ObjectMapper): Jackson2JsonDecoder =
        Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON)

    @Bean
    fun webFluxConfigurer(encoder: Jackson2JsonEncoder, decoder: Jackson2JsonDecoder): WebFluxConfigurer {
        return object : WebFluxConfigurer {
            override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
                configurer.defaultCodecs().jackson2JsonEncoder(encoder)
                configurer.defaultCodecs().jackson2JsonDecoder(decoder)
            }
        }
    }

    fun jsonExchangeStrategyHelper(
        objectMapper: ObjectMapper,
        jackson2JsonEncoder: Jackson2JsonEncoder,
        jackson2JsonDecoder: Jackson2JsonDecoder,
    ): ExchangeStrategies {
        return ExchangeStrategies
            .builder()
            .codecs { clientDefaultCodecsConfigurer: ClientCodecConfigurer ->
                clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(
                    Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON),
                )
                clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(
                    Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON),
                )
                clientDefaultCodecsConfigurer.defaultCodecs()
                    .maxInMemorySize(ofMegabytes(MAX_MEMORY_IN_MB).toBytes().toInt())
            }.build()
    }
}

@ConfigurationProperties(prefix = "http-config")
class HttpConnectionProperties {
    lateinit var connectionTimeout: String
}