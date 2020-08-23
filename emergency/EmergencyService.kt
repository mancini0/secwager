package com.secwager.emergency

import java.lang.Exception
import java.time.LocalDateTime

interface EmergencyService {

    fun requestIntervention(problemDescription:String,
                            timestamp: LocalDateTime,
                            userId : String?,
                            preceedingException: Throwable?)
}