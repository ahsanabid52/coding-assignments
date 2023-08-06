package de.vanguard.rebalancingservice.service.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties(prefix = "fps.api")
data class FPSProperties(var url: String, var batchSize: String)