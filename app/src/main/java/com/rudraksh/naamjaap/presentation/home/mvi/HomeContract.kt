package com.rudraksh.naamjaap.presentation.home.mvi

import com.rudraksh.naamjaap.core.mvi.MviEffect
import com.rudraksh.naamjaap.core.mvi.MviIntent
import com.rudraksh.naamjaap.core.mvi.MviResult
import com.rudraksh.naamjaap.core.mvi.MviState

data class HomeState(
    val isLoading: Boolean = true,
    
    // Resume Session State
    val hasActiveSession: Boolean = false,
    val resumeSessionMantra: String? = null,
    val resumeSessionCount: Int = 0,
    val resumeSessionTarget: Int = 108,
    
    // New Session State
    val newSessionMantraInput: String = "",
    val newSessionTargetInput: String = "108",
    val defaultTarget: Int = 108,
    
    // Dialog & Validation
    val isCustomTargetDialogVisible: Boolean = false,
    val targetValidationError: String? = null
) : MviState

sealed class HomeIntent : MviIntent {
    object LoadHomeData : HomeIntent()
    
    data class UpdateMantraInput(val mantra: String) : HomeIntent()
    object ShowCustomTargetDialog : HomeIntent()
    object HideCustomTargetDialog : HomeIntent()
    data class SubmitCustomTarget(val targetStr: String) : HomeIntent()
    
    object StartNewSession : HomeIntent()
    object ResumeSession : HomeIntent()
    
    object NavigateToHistory : HomeIntent()
    object NavigateToSettings : HomeIntent()
}

sealed class HomeResult : MviResult {
    object Loading : HomeResult()
    data class HomeDataLoaded(
        val defaultTarget: Int,
        val hasActiveSession: Boolean,
        val resumeMantra: String?,
        val resumeCount: Int,
        val resumeTarget: Int
    ) : HomeResult()
    
    data class MantraInputUpdated(val mantra: String) : HomeResult()
    object CustomTargetDialogShown : HomeResult()
    object CustomTargetDialogHidden : HomeResult()
    
    data class TargetValidationFailed(val message: String) : HomeResult()
    data class TargetValidationSuccess(val target: Int) : HomeResult()
}

sealed class HomeEffect : MviEffect {
    object NavigateToSession : HomeEffect()
    object NavigateToHistory : HomeEffect()
    object NavigateToSettings : HomeEffect()
    data class ShowToast(val message: String) : HomeEffect()
}
