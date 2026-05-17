package com.rudraksh.naamjaap.presentation.session.mvi

import com.rudraksh.naamjaap.core.mvi.Reducer
import javax.inject.Inject

class SessionReducer @Inject constructor() : Reducer<SessionState, SessionResult> {
    override fun reduce(state: SessionState, result: SessionResult): SessionState {
        return when (result) {
            is SessionResult.Loading -> {
                state.copy(isLoading = true)
            }
            is SessionResult.InitialDataLoaded -> {
                state.copy(
                    isLoading = false,
                    currentCount = result.session.currentCount,
                    targetCount = result.session.targetCount,
                    activeBeadIndex = result.session.activeBeadIndex,
                    direction = result.session.direction,
                    mantra = result.session.mantra,
                    isPaused = result.session.isPaused,
                    guruBlockingState = result.session.guruBlockingState,
                    activeDurationMillis = result.session.accumulatedActiveDuration,
                    hapticEnabled = result.hapticEnabled,
                    soundEnabled = result.soundEnabled
                )
            }
            is SessionResult.SessionUpdated -> {
                state.copy(
                    currentCount = result.session.currentCount,
                    targetCount = result.session.targetCount,
                    activeBeadIndex = result.session.activeBeadIndex,
                    direction = result.session.direction,
                    isPaused = result.session.isPaused,
                    guruBlockingState = result.session.guruBlockingState,
                    activeDurationMillis = result.session.accumulatedActiveDuration
                )
            }
            is SessionResult.TimerTick -> {
                state.copy(activeDurationMillis = result.activeDurationMillis)
            }
            is SessionResult.SessionCompleted -> {
                state.copy(isCompleted = true)
            }
        }
    }
}
