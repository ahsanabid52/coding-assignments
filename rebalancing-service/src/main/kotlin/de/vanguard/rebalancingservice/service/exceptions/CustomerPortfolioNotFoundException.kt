package de.vanguard.rebalancingservice.service.exceptions



class CustomerPortfolioNotFoundException(override val message: String? = "Customer Portfolio Not Found.") : RuntimeException(message)