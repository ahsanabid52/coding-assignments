package de.element.travelguideservice.hiker.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HikerRepository : CrudRepository<HikerEntity, UUID>