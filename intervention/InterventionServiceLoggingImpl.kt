package com.secwager.intervention

import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class InterventionServiceLoggingImpl : InterventionService {
    companion object {
        private val log = LoggerFactory.getLogger(InterventionServiceLoggingImpl::class.java)
    }

    override fun requestIntervention(problemDescription: String, timestamp: LocalDateTime, userId: String?, preceedingException: Throwable?) {
        log.error("{}, {}, {},  {}" ,problemDescription, timestamp, userId, preceedingException)
    }
}