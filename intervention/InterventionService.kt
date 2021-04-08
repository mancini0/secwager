package com.secwager.intervention

import java.lang.Exception
import java.time.LocalDateTime

interface InterventionService {

    fun requestIntervention(problemDescription:String,
                            timestamp: LocalDateTime,
                            userId : String?,
                            preceedingException: Throwable?)
}