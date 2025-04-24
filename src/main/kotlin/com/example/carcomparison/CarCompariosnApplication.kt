package com.example.carcomparison

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing // Enable JPA Auditing
class CarCompariosnApplication

fun main(args: Array<String>) {
    runApplication<CarCompariosnApplication>(*args)
}
