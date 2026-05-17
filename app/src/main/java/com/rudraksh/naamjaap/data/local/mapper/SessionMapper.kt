package com.rudraksh.naamjaap.data.local.mapper

import com.rudraksh.naamjaap.data.local.entity.SessionEntity
import com.rudraksh.naamjaap.domain.model.JapSession

fun SessionEntity.toDomain(): JapSession {
    return JapSession(
        id = id,
        targetCount = targetCount,
        completedCount = completedCount,
        mantra = mantra,
        durationMillis = durationMillis,
        timestamp = timestamp,
        isCompleted = isCompleted
    )
}

fun JapSession.toEntity(): SessionEntity {
    return SessionEntity(
        id = id,
        targetCount = targetCount,
        completedCount = completedCount,
        mantra = mantra,
        durationMillis = durationMillis,
        timestamp = timestamp,
        isCompleted = isCompleted
    )
}
