package de.vanguard.rebalancingservice.service.exceptions



class InvalidStrategyProvidedForTheCustomer(override val message: String? = "No Suitable Strategy Found for Customer.") : RuntimeException(message)