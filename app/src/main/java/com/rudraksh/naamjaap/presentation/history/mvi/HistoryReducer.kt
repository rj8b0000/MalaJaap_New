package com.rudraksh.naamjaap.presentation.history.mvi

import com.rudraksh.naamjaap.core.mvi.Reducer
import javax.inject.Inject

class HistoryReducer @Inject constructor() : Reducer<HistoryState, HistoryResult> {
    override fun reduce(state: HistoryState, result: HistoryResult): HistoryState {
        return when (result) {
            is HistoryResult.Loading -> state.copy(isLoading = true)
            is HistoryResult.HistoryLoaded -> state.copy(
                isLoading = false,
                sessions = result.sessions,
                totalCount = result.totalCount
            )
        }
    }
}
