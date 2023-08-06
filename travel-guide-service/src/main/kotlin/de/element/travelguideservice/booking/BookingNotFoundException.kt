package de.element.travelguideservice.booking

class BookingNotFoundException(override val message: String? = "Booking not found for the provided id.") :
    RuntimeException(message)