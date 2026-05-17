package com.rudraksh.naamjaap.presentation.home.mvi

import com.rudraksh.naamjaap.core.mvi.Reducer
import javax.inject.Inject

class HomeReducer @Inject constructor() : Reducer<HomeState, HomeResult> {
    override fun reduce(state: HomeState, result: HomeResult): HomeState {
        return when (result) {
            is HomeResult.Loading -> {
                state.copy(isLoading = true)
            }
            is HomeResult.HomeDataLoaded -> {
                state.copy(
                    isLoading = false,
                    defaultTarget = result.defaultTarget,
                    hasActiveSession = result.hasActiveSession,
                    resumeSessionMantra = result.resumeMantra,
                    resumeSessionCount = result.resumeCount,
                    resumeSessionTarget = result.resumeTarget,
                    newSessionTargetInput = if (state.newSessionTargetInput == "108" && result.defaultTarget != 108) 
                                                result.defaultTarget.toString() 
                                            else state.newSessionTargetInput
                )
            }
            is HomeResult.MantraInputUpdated -> {
                state.copy(newSessionMantraInput = result.mantra)
            }
            is HomeResult.CustomTargetDialogShown -> {
                state.copy(isCustomTargetDialogVisible = true, targetValidationError = null)
            }
            is HomeResult.CustomTargetDialogHidden -> {
                state.copy(isCustomTargetDialogVisible = false, targetValidationError = null)
            }
            is HomeResult.TargetValidationFailed -> {
                state.copy(targetValidationError = result.message)
            }
            is HomeResult.TargetValidationSuccess -> {
                state.copy(
                    targetValidationError = null,
                    isCustomTargetDialogVisible = false,
                    newSessionTargetInput = result.target.toString()
                )
            }
        }
    }
}
