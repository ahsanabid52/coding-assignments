package de.element.travelguideservice.booking

class AgeLimitException(override val message: String? = "Age is not valid for this trail.") : RuntimeException(message)