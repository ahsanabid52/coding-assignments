package de.vanguard.rebalancingservice.service.exceptions



class UnableToExecuteCustomerTradesException(override val message: String? = "Unable To Execute Customer Trades.") : RuntimeException(message)