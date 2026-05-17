package com.rudraksh.naamjaap.presentation.history.mvi

import com.rudraksh.naamjaap.core.mvi.MviEffect
import com.rudraksh.naamjaap.core.mvi.MviIntent
import com.rudraksh.naamjaap.core.mvi.MviResult
import com.rudraksh.naamjaap.core.mvi.MviState
import com.rudraksh.naamjaap.domain.model.JapSession

data class HistoryState(
    val isLoading: Boolean = true,
    val sessions: List<JapSession> = emptyList(),
    val totalCount: Int = 0
) : MviState

sealed class HistoryIntent : MviIntent {
    object LoadHistory : HistoryIntent()
    object NavigateBack : HistoryIntent()
}

sealed class HistoryResult : MviResult {
    object Loading : HistoryResult()
    data class HistoryLoaded(
        val sessions: List<JapSession>,
        val totalCount: Int
    ) : HistoryResult()
}

sealed class HistoryEffect : MviEffect {
    object NavigateBack : HistoryEffect()
}
