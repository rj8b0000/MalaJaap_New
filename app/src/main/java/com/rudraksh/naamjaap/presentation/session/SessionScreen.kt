package com.rudraksh.naamjaap.presentation.session

import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.rudraksh.naamjaap.presentation.common.components.NjButton
import com.rudraksh.naamjaap.presentation.common.components.NjOutlinedButton
import com.rudraksh.naamjaap.presentation.session.components.MalaWheel
import com.rudraksh.naamjaap.presentation.session.components.SessionHud
import com.rudraksh.naamjaap.presentation.session.mvi.SessionEffect
import com.rudraksh.naamjaap.presentation.session.mvi.SessionIntent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSummary: (count: Int, target: Int, durationMillis: Long, mantra: String?) -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    
    // Simple tone generator for audio feedback without requiring raw assets
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }
    DisposableEffect(Unit) {
        onDispose {
            toneGenerator.release()
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SessionEffect.PlayHapticTick -> {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                is SessionEffect.PlaySoundTick -> {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
                }
                is SessionEffect.PlayGuruBeadEffect -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    toneGenerator.startTone(ToneGenerator.TONE_SUP_ERROR, 100)
                }
                is SessionEffect.PlayCompletionEffect -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 300)
                }
                is SessionEffect.ShowGuruBeadExplanation -> {
                    Toast.makeText(context, "Guru Bead reached. Direction reversed.", Toast.LENGTH_SHORT).show()
                }
                is SessionEffect.NavigateToCompletionSummary -> {
                    onNavigateToSummary(state.currentCount, state.targetCount, state.activeDurationMillis, state.mantra)
                }
                is SessionEffect.NavigateBackToHome -> {
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.mantra ?: "Naam Jaap", color = MaterialTheme.colorScheme.primary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SessionHud(
                currentCount = state.currentCount,
                targetCount = state.targetCount,
                activeDurationMillis = state.activeDurationMillis,
                modifier = Modifier.weight(0.2f)
            )

            MalaWheel(
                activeBeadIndex = state.activeBeadIndex,
                direction = state.direction,
                isPaused = state.isPaused || state.isCompleted,
                onSwipeBead = { viewModel.processIntent(SessionIntent.SwipeBead) },
                modifier = Modifier.weight(0.6f)
            )

            Row(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NjOutlinedButton(
                    text = "End",
                    onClick = { viewModel.processIntent(SessionIntent.EndSession) },
                    modifier = Modifier.weight(1f)
                )
                NjButton(
                    text = if (state.isPaused) "Resume" else "Pause",
                    onClick = { viewModel.processIntent(SessionIntent.TogglePauseResume) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
