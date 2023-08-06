package de.vanguard.rebalancingservice.service.exceptions



class BatchSizeLimitForCustomerTradesExceededException(override val message: String? = "Batch Size Limit For Customer Trades Exceeded Exception.") : RuntimeException(message)