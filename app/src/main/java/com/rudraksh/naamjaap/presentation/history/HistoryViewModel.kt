package com.rudraksh.naamjaap.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksh.naamjaap.domain.usecase.GetSessionHistoryUseCase
import com.rudraksh.naamjaap.presentation.history.mvi.HistoryEffect
import com.rudraksh.naamjaap.presentation.history.mvi.HistoryIntent
import com.rudraksh.naamjaap.presentation.history.mvi.HistoryReducer
import com.rudraksh.naamjaap.presentation.history.mvi.HistoryResult
import com.rudraksh.naamjaap.presentation.history.mvi.HistoryState
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
class HistoryViewModel @Inject constructor(
    private val getSessionHistoryUseCase: GetSessionHistoryUseCase,
    private val reducer: HistoryReducer
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HistoryEffect>()
    val effect: SharedFlow<HistoryEffect> = _effect.asSharedFlow()

    init {
        processIntent(HistoryIntent.LoadHistory)
    }

    fun processIntent(intent: HistoryIntent) {
        when (intent) {
            is HistoryIntent.LoadHistory -> loadHistory()
            is HistoryIntent.NavigateBack -> emitEffect(HistoryEffect.NavigateBack)
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            updateState(HistoryResult.Loading)
            getSessionHistoryUseCase().collectLatest { sessions ->
                val total = sessions.sumOf { it.completedCount }
                updateState(HistoryResult.HistoryLoaded(sessions, total))
            }
        }
    }

    private fun updateState(result: HistoryResult) {
        _state.value = reducer.reduce(_state.value, result)
    }

    private fun emitEffect(effect: HistoryEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
