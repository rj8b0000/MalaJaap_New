package com.rudraksh.naamjaap.presentation.settings.mvi

import com.rudraksh.naamjaap.core.mvi.Reducer
import javax.inject.Inject

class SettingsReducer @Inject constructor() : Reducer<SettingsState, SettingsResult> {
    override fun reduce(state: SettingsState, result: SettingsResult): SettingsState {
        return when (result) {
            is SettingsResult.Loading -> {
                state.copy(isLoading = true)
            }
            is SettingsResult.SettingsLoaded -> {
                state.copy(
                    isLoading = false,
                    hapticEnabled = result.settings.hapticEnabled,
                    soundEnabled = result.settings.soundEnabled,
                    defaultTarget = result.settings.defaultTarget,
                    targetInputError = null
                )
            }
            is SettingsResult.TargetValidationError -> {
                state.copy(targetInputError = result.message)
            }
            is SettingsResult.TargetValidationCleared -> {
                state.copy(targetInputError = null)
            }
        }
    }
}
