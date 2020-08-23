package com.secwager.emergency

import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class EmergencyServiceLoggingImpl : EmergencyService {
    companion object {
        private val log = LoggerFactory.getLogger(EmergencyServiceLoggingImpl::class.java)
    }

    override fun requestIntervention(problemDescription: String, timestamp: LocalDateTime, userId: String?, preceedingException: Throwable?) {
        log.error("{}, {}, {},  {}" ,problemDescription, timestamp, userId, preceedingException)
    }
}