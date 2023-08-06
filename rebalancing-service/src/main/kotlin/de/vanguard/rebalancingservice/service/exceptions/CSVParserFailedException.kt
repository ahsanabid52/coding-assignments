package de.vanguard.rebalancingservice.service.exceptions

class CSVParserFailedException(override val message: String? = "CSV Parser failed.") : RuntimeException(message)