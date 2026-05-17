package com.rudraksh.naamjaap.domain.usecase

import com.rudraksh.naamjaap.domain.model.ActiveSession
import com.rudraksh.naamjaap.domain.model.JapSession
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(activeSession: ActiveSession) {
        val isCompleted = activeSession.currentCount >= activeSession.targetCount
        
        val historySession = JapSession(
            targetCount = activeSession.targetCount,
            completedCount = activeSession.currentCount,
            mantra = activeSession.mantra,
            durationMillis = activeSession.accumulatedActiveDuration,
            timestamp = System.currentTimeMillis(),
            isCompleted = isCompleted
        )
        
        // Save the final result to the history database
        sessionRepository.saveSessionHistory(historySession)
        
        // Clear the transient active session from preferences
        sessionRepository.clearActiveSession()
    }
}
