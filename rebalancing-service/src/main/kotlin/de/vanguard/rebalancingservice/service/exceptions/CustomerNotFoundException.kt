package de.vanguard.rebalancingservice.service.exceptions



class CustomerNotFoundException(override val message: String? = "Customer Not Found.") : RuntimeException(message)