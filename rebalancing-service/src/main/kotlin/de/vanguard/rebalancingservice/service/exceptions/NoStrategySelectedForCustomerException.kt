package de.vanguard.rebalancingservice.service.exceptions



class NoStrategySelectedForCustomerException(override val message: String? = "Strategy Not Selected For the Customer.") : RuntimeException(message)