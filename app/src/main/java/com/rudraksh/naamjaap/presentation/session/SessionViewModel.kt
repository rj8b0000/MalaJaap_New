package com.rudraksh.naamjaap.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksh.naamjaap.domain.model.ActiveSession
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import com.rudraksh.naamjaap.domain.usecase.GetSettingsUseCase
import com.rudraksh.naamjaap.domain.usecase.IncrementJapUseCase
import com.rudraksh.naamjaap.domain.usecase.SaveSessionUseCase
import com.rudraksh.naamjaap.presentation.session.mvi.SessionEffect
import com.rudraksh.naamjaap.presentation.session.mvi.SessionIntent
import com.rudraksh.naamjaap.presentation.session.mvi.SessionReducer
import com.rudraksh.naamjaap.presentation.session.mvi.SessionResult
import com.rudraksh.naamjaap.presentation.session.mvi.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val incrementJapUseCase: IncrementJapUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val reducer: SessionReducer
) : ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    val state: StateFlow<SessionState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SessionEffect>()
    val effect: SharedFlow<SessionEffect> = _effect.asSharedFlow()

    private var timerJob: Job? = null
    
    // Hold the raw domain model internally as the source of truth for pure UseCases
    private var currentActiveSession: ActiveSession? = null

    init {
        processIntent(SessionIntent.LoadSessionData)
    }

    fun processIntent(intent: SessionIntent) {
        when (intent) {
            is SessionIntent.LoadSessionData -> loadSessionData()
            is SessionIntent.SwipeBeadForward -> handleSwipeForward()
            is SessionIntent.SwipeBeadBackward -> handleSwipeBackward()
            is SessionIntent.TogglePauseResume -> togglePauseResume()
            is SessionIntent.EndSession -> endSession()
            is SessionIntent.AppBackgrounded -> handleAppBackgrounded()
        }
    }

    private fun loadSessionData() {
        viewModelScope.launch {
            updateState(SessionResult.Loading)
            val settings = getSettingsUseCase().first()
            val activeSession = sessionRepository.getActiveSession()
            
            if (activeSession != null) {
                currentActiveSession = activeSession
                updateState(
                    SessionResult.InitialDataLoaded(
                        session = activeSession,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled
                    )
                )
                
                if (!activeSession.isPaused) {
                    startTimer()
                }
            } else {
                emitEffect(SessionEffect.NavigateBackToHome)
            }
        }
    }

    private fun handleSwipeForward() {
        val session = currentActiveSession ?: return
        if (session.isPaused || _state.value.isCompleted) return

        val newSession = incrementJapUseCase(session)
        
        val wasGuruBeadHit = !session.guruBlockingState && newSession.guruBlockingState
        val wasNormalIncrement = newSession.currentCount > session.currentCount
        val didCompleteTarget = newSession.currentCount >= newSession.targetCount && session.currentCount < session.targetCount

        currentActiveSession = newSession
        updateState(SessionResult.SessionUpdated(newSession))
        persistSession(newSession)

        if (wasGuruBeadHit) {
            emitEffect(SessionEffect.PlayGuruBeadEffect)
            emitEffect(SessionEffect.ShowGuruBeadExplanation)
        } else if (wasNormalIncrement) {
            if (_state.value.hapticEnabled) emitEffect(SessionEffect.PlayHapticTick)
            if (_state.value.soundEnabled) emitEffect(SessionEffect.PlaySoundTick)
            
            if (didCompleteTarget) {
                updateState(SessionResult.SessionCompleted)
                stopTimer()
                viewModelScope.launch {
                    saveSessionUseCase(newSession)
                    emitEffect(SessionEffect.PlayCompletionEffect)
                    emitEffect(SessionEffect.NavigateToCompletionSummary)
                }
            }
        }
    }

    private fun handleSwipeBackward() {
        val session = currentActiveSession ?: return
        if (session.isPaused || _state.value.isCompleted || session.currentCount <= 0) return

        var nextCount = session.currentCount
        var nextBeadIndex = session.activeBeadIndex
        var nextDirection = session.direction
        var hitGuruBead = false

        val attemptedPrevBead = nextBeadIndex - nextDirection

        if (attemptedPrevBead > 108 || attemptedPrevBead < 1) {
            nextDirection *= -1
            hitGuruBead = true
        } else {
            nextCount--
            nextBeadIndex = attemptedPrevBead
            hitGuruBead = false
        }

        val newSession = session.copy(
            currentCount = nextCount,
            activeBeadIndex = nextBeadIndex,
            direction = nextDirection,
            guruBlockingState = hitGuruBead
        )

        currentActiveSession = newSession
        updateState(SessionResult.SessionUpdated(newSession))
        persistSession(newSession)

        if (hitGuruBead) {
            emitEffect(SessionEffect.PlayGuruBeadEffect)
            emitEffect(SessionEffect.ShowGuruBeadExplanation)
        } else {
            if (_state.value.hapticEnabled) emitEffect(SessionEffect.PlayHapticTick)
            if (_state.value.soundEnabled) emitEffect(SessionEffect.PlaySoundTick)
        }
    }

    private fun togglePauseResume() {
        val session = currentActiveSession ?: return
        if (_state.value.isCompleted) return
        
        val newPausedState = !session.isPaused
        val newSession = session.copy(isPaused = newPausedState)
        
        currentActiveSession = newSession
        updateState(SessionResult.SessionUpdated(newSession))
        persistSession(newSession)
        
        if (newPausedState) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun handleAppBackgrounded() {
        val session = currentActiveSession ?: return
        if (session.isPaused || _state.value.isCompleted) {
            persistSession(session)
            return
        }
        
        // Auto-pause the session to prevent background timer accumulation
        val newSession = session.copy(isPaused = true)
        currentActiveSession = newSession
        updateState(SessionResult.SessionUpdated(newSession))
        persistSession(newSession)
        stopTimer()
    }

    /**
     * Manually ending the session before the target is reached, or after completion summary.
     */
    private fun endSession() {
        val session = currentActiveSession ?: return
        viewModelScope.launch {
            stopTimer()
            saveSessionUseCase(session)
            emitEffect(SessionEffect.NavigateBackToHome)
        }
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val session = currentActiveSession ?: break
                val newDuration = session.accumulatedActiveDuration + 1000
                currentActiveSession = session.copy(accumulatedActiveDuration = newDuration)
                updateState(SessionResult.TimerTick(newDuration))
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    /**
     * Non-blocking flush of the active session state to DataStore.
     * Called after every increment and on pause/resume to guarantee survival across process death.
     */
    private fun persistSession(session: ActiveSession) {
        viewModelScope.launch {
            sessionRepository.saveActiveSession(session)
        }
    }

    private fun updateState(result: SessionResult) {
        _state.value = reducer.reduce(_state.value, result)
    }

    private fun emitEffect(effect: SessionEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
