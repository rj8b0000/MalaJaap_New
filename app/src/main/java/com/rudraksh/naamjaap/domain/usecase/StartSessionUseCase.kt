package com.rudraksh.naamjaap.domain.usecase

import com.rudraksh.naamjaap.domain.model.ActiveSession
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import javax.inject.Inject

class StartSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(targetCount: Int, mantra: String?): ActiveSession {
        // Clear any existing active session from DataStore
        sessionRepository.clearActiveSession()
        
        val newSession = ActiveSession(
            mantra = mantra?.takeIf { it.isNotBlank() },
            targetCount = targetCount,
            currentCount = 0,
            activeBeadIndex = 1,
            direction = 1,
            guruBlockingState = false,
            accumulatedActiveDuration = 0L,
            sessionStartEpoch = System.currentTimeMillis(),
            isPaused = false
        )
        
        sessionRepository.saveActiveSession(newSession)
        return newSession
    }
}
