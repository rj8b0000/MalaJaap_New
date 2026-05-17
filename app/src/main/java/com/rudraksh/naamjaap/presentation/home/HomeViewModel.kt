package com.rudraksh.naamjaap.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import com.rudraksh.naamjaap.domain.usecase.GetSettingsUseCase
import com.rudraksh.naamjaap.domain.usecase.StartSessionUseCase
import com.rudraksh.naamjaap.domain.usecase.ValidateCustomTargetUseCase
import com.rudraksh.naamjaap.presentation.home.mvi.HomeEffect
import com.rudraksh.naamjaap.presentation.home.mvi.HomeIntent
import com.rudraksh.naamjaap.presentation.home.mvi.HomeReducer
import com.rudraksh.naamjaap.presentation.home.mvi.HomeResult
import com.rudraksh.naamjaap.presentation.home.mvi.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
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
class HomeViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val sessionRepository: SessionRepository,
    private val startSessionUseCase: StartSessionUseCase,
    private val validateCustomTargetUseCase: ValidateCustomTargetUseCase,
    private val reducer: HomeReducer
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        processIntent(HomeIntent.LoadHomeData)
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadHomeData -> loadHomeData()
            is HomeIntent.UpdateMantraInput -> updateState(HomeResult.MantraInputUpdated(intent.mantra))
            is HomeIntent.ShowCustomTargetDialog -> updateState(HomeResult.CustomTargetDialogShown)
            is HomeIntent.HideCustomTargetDialog -> updateState(HomeResult.CustomTargetDialogHidden)
            is HomeIntent.SubmitCustomTarget -> validateAndSubmitCustomTarget(intent.targetStr)
            is HomeIntent.StartNewSession -> startNewSession()
            is HomeIntent.ResumeSession -> emitEffect(HomeEffect.NavigateToSession)
            is HomeIntent.NavigateToHistory -> emitEffect(HomeEffect.NavigateToHistory)
            is HomeIntent.NavigateToSettings -> emitEffect(HomeEffect.NavigateToSettings)
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            updateState(HomeResult.Loading)
            val settings = getSettingsUseCase().first()
            val activeSession = sessionRepository.getActiveSession()
            
            updateState(
                HomeResult.HomeDataLoaded(
                    defaultTarget = settings.defaultTarget,
                    hasActiveSession = activeSession != null,
                    resumeMantra = activeSession?.mantra,
                    resumeCount = activeSession?.currentCount ?: 0,
                    resumeTarget = activeSession?.targetCount ?: 0
                )
            )
        }
    }

    private fun validateAndSubmitCustomTarget(targetStr: String) {
        viewModelScope.launch {
            val validation = validateCustomTargetUseCase(targetStr)
            if (validation.isSuccess) {
                updateState(HomeResult.TargetValidationSuccess(validation.getOrThrow()))
            } else {
                updateState(HomeResult.TargetValidationFailed(validation.exceptionOrNull()?.message ?: "Invalid Target"))
            }
        }
    }

    private fun startNewSession() {
        viewModelScope.launch {
            val target = _state.value.newSessionTargetInput.toIntOrNull() ?: 108
            val mantra = _state.value.newSessionMantraInput.trim()
            
            startSessionUseCase(targetCount = target, mantra = mantra.ifEmpty { null })
            
            // Reload home data internally just in case they navigate back immediately
            loadHomeData()
            
            emitEffect(HomeEffect.NavigateToSession)
        }
    }

    private fun updateState(result: HomeResult) {
        _state.value = reducer.reduce(_state.value, result)
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
