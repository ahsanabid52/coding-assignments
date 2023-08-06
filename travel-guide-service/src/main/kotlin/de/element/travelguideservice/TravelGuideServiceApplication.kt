package de.element.travelguideservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TravelGuideServiceApplication

fun main(args: Array<String>) {
    runApplication<TravelGuideServiceApplication>(*args)
}