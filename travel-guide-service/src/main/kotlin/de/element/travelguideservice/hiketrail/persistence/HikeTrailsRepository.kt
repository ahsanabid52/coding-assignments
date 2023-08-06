package de.element.travelguideservice.hiketrail.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HikeTrailsRepository : CrudRepository<HikeTrailsEntity, UUID>