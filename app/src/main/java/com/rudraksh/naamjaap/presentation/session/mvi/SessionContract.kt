package com.rudraksh.naamjaap.presentation.session.mvi

import com.rudraksh.naamjaap.core.mvi.MviEffect
import com.rudraksh.naamjaap.core.mvi.MviIntent
import com.rudraksh.naamjaap.core.mvi.MviResult
import com.rudraksh.naamjaap.core.mvi.MviState
import com.rudraksh.naamjaap.domain.model.ActiveSession

data class SessionState(
    val isLoading: Boolean = true,
    
    val currentCount: Int = 0,
    val targetCount: Int = 108,
    val activeBeadIndex: Int = 1,
    val direction: Int = 1,
    val mantra: String? = null,
    
    val isPaused: Boolean = false,
    val isCompleted: Boolean = false,
    val guruBlockingState: Boolean = false,
    
    val activeDurationMillis: Long = 0L,
    
    val hapticEnabled: Boolean = true,
    val soundEnabled: Boolean = true
) : MviState

sealed class SessionIntent : MviIntent {
    object LoadSessionData : SessionIntent()
    object SwipeBeadForward : SessionIntent()
    object SwipeBeadBackward : SessionIntent()
    object TogglePauseResume : SessionIntent()
    object EndSession : SessionIntent()
    object AppBackgrounded : SessionIntent()
}

sealed class SessionResult : MviResult {
    object Loading : SessionResult()
    
    data class InitialDataLoaded(
        val session: ActiveSession,
        val hapticEnabled: Boolean,
        val soundEnabled: Boolean
    ) : SessionResult()
    
    data class SessionUpdated(val session: ActiveSession) : SessionResult()
    data class TimerTick(val activeDurationMillis: Long) : SessionResult()
    object SessionCompleted : SessionResult()
}

sealed class SessionEffect : MviEffect {
    object PlayHapticTick : SessionEffect()
    object PlaySoundTick : SessionEffect()
    object PlayGuruBeadEffect : SessionEffect()
    object PlayCompletionEffect : SessionEffect()
    
    object ShowGuruBeadExplanation : SessionEffect()
    object NavigateToCompletionSummary : SessionEffect()
    object NavigateBackToHome : SessionEffect()
}
