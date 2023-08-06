package de.vanguard.rebalancingservice.service.client

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class FPSClient(private val restTemplate: RestTemplate, private val fpsProperties: FPSProperties) {
    private val logger = KotlinLogging.logger {}

    fun getCustomerInfo(customerId: String): Optional<CustomerPortfolioDto> {
        logger.info { "Going to get the customer $customerId portfolio information from the FPS $fpsProperties.url." }
        try {
            return Optional.of(
                restTemplate.getForEntity(
                    fpsProperties.url.plus("/customer/$customerId"),
                    CustomerPortfolioDto::class.java
                ).body!!
            )
        } catch (exception: Exception) {
            logger.error { "Unable to get the customer information for the customer $customerId " }
        }
        return Optional.empty()
    }

    fun executeTradesBatch(customerTrades: List<CustomerTradeDto>): ResponseEntity<Void> {
        logger.info { "Going to execute the trades $customerTrades in the FPS." }
        try {
            return restTemplate.postForEntity(fpsProperties.url.plus("/execute/"), customerTrades, Void::class.java)
        } catch (exception: Exception) {
            logger.error { "Unable to execute the customer trades $customerTrades " }
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}