package de.element.travelguideservice.hiketrail

class HikeTrailNotFoundException(override val message: String? = "Hike Trail not found.") : RuntimeException(message)