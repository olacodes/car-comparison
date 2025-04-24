package com.example.carcomparison

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<CarCompariosnApplication>().with(TestcontainersConfiguration::class).run(*args)
}
