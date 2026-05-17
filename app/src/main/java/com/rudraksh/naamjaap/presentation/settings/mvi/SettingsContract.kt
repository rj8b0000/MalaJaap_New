package com.rudraksh.naamjaap.presentation.settings.mvi

import com.rudraksh.naamjaap.core.mvi.MviEffect
import com.rudraksh.naamjaap.core.mvi.MviIntent
import com.rudraksh.naamjaap.core.mvi.MviResult
import com.rudraksh.naamjaap.core.mvi.MviState
import com.rudraksh.naamjaap.domain.model.Settings

data class SettingsState(
    val isLoading: Boolean = true,
    val hapticEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val defaultTarget: Int = 108,
    val targetInputError: String? = null
) : MviState

sealed class SettingsIntent : MviIntent {
    object LoadSettings : SettingsIntent()
    data class ToggleHaptic(val enabled: Boolean) : SettingsIntent()
    data class ToggleSound(val enabled: Boolean) : SettingsIntent()
    data class UpdateDefaultTarget(val targetStr: String) : SettingsIntent()
    object NavigateBack : SettingsIntent()
}

sealed class SettingsResult : MviResult {
    object Loading : SettingsResult()
    data class SettingsLoaded(val settings: Settings) : SettingsResult()
    data class TargetValidationError(val message: String) : SettingsResult()
    object TargetValidationCleared : SettingsResult()
}

sealed class SettingsEffect : MviEffect {
    object NavigateBack : SettingsEffect()
    data class ShowToast(val message: String) : SettingsEffect()
}
