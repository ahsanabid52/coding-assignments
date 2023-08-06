package de.vanguard.rebalancingservice.service.exceptions



class StrategyNotFoundException(override val message: String? = "Strategy Not Found.") : RuntimeException(message)