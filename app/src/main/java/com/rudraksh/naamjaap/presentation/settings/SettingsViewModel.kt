package com.rudraksh.naamjaap.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksh.naamjaap.domain.model.Settings
import com.rudraksh.naamjaap.domain.usecase.GetSettingsUseCase
import com.rudraksh.naamjaap.domain.usecase.UpdateSettingsUseCase
import com.rudraksh.naamjaap.domain.usecase.ValidateCustomTargetUseCase
import com.rudraksh.naamjaap.presentation.settings.mvi.SettingsEffect
import com.rudraksh.naamjaap.presentation.settings.mvi.SettingsIntent
import com.rudraksh.naamjaap.presentation.settings.mvi.SettingsReducer
import com.rudraksh.naamjaap.presentation.settings.mvi.SettingsResult
import com.rudraksh.naamjaap.presentation.settings.mvi.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val validateCustomTargetUseCase: ValidateCustomTargetUseCase,
    private val reducer: SettingsReducer
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect: SharedFlow<SettingsEffect> = _effect.asSharedFlow()

    init {
        processIntent(SettingsIntent.LoadSettings)
    }

    fun processIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.LoadSettings -> loadSettings()
            is SettingsIntent.ToggleHaptic -> toggleHaptic(intent.enabled)
            is SettingsIntent.ToggleSound -> toggleSound(intent.enabled)
            is SettingsIntent.UpdateDefaultTarget -> updateTarget(intent.targetStr)
            is SettingsIntent.NavigateBack -> emitEffect(SettingsEffect.NavigateBack)
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            updateState(SettingsResult.Loading)
            getSettingsUseCase().collectLatest { settings ->
                updateState(SettingsResult.SettingsLoaded(settings))
            }
        }
    }

    private fun toggleHaptic(enabled: Boolean) {
        viewModelScope.launch {
            val currentSettings = getCurrentSettings()
            updateSettingsUseCase(currentSettings.copy(hapticEnabled = enabled))
        }
    }

    private fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            val currentSettings = getCurrentSettings()
            updateSettingsUseCase(currentSettings.copy(soundEnabled = enabled))
        }
    }

    private fun updateTarget(targetStr: String) {
        viewModelScope.launch {
            val validationResult = validateCustomTargetUseCase(targetStr)
            if (validationResult.isSuccess) {
                val validTarget = validationResult.getOrThrow()
                val currentSettings = getCurrentSettings()
                updateSettingsUseCase(currentSettings.copy(defaultTarget = validTarget))
                updateState(SettingsResult.TargetValidationCleared)
                emitEffect(SettingsEffect.ShowToast("Target updated to $validTarget"))
            } else {
                updateState(SettingsResult.TargetValidationError(validationResult.exceptionOrNull()?.message ?: "Invalid target"))
            }
        }
    }

    private fun getCurrentSettings(): Settings {
        return Settings(
            hapticEnabled = _state.value.hapticEnabled,
            soundEnabled = _state.value.soundEnabled,
            defaultTarget = _state.value.defaultTarget
        )
    }

    private fun updateState(result: SettingsResult) {
        _state.value = reducer.reduce(_state.value, result)
    }

    private fun emitEffect(effect: SettingsEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
