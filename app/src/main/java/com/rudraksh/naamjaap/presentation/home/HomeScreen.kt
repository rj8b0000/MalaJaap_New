package com.rudraksh.naamjaap.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.List
import com.rudraksh.naamjaap.presentation.common.components.NjButton
import com.rudraksh.naamjaap.presentation.common.components.NjCard
import com.rudraksh.naamjaap.presentation.common.components.NjDialog
import com.rudraksh.naamjaap.presentation.home.mvi.HomeEffect
import com.rudraksh.naamjaap.presentation.home.mvi.HomeIntent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSession: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.NavigateToSession -> onNavigateToSession()
                is HomeEffect.NavigateToHistory -> onNavigateToHistory()
                is HomeEffect.NavigateToSettings -> onNavigateToSettings()
                is HomeEffect.ShowToast -> { /* Show Toast if needed */ }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(HomeIntent.LoadHomeData)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Naam Jaap", color = MaterialTheme.colorScheme.primary) },
                actions = {
                    IconButton(onClick = { viewModel.processIntent(HomeIntent.NavigateToHistory) }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "History", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    IconButton(onClick = { viewModel.processIntent(HomeIntent.NavigateToSettings) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (state.hasActiveSession) {
                ResumeSessionCard(
                    mantra = state.resumeSessionMantra,
                    currentCount = state.resumeSessionCount,
                    targetCount = state.resumeSessionTarget,
                    onResumeClick = { viewModel.processIntent(HomeIntent.ResumeSession) }
                )
            }

            NewSessionCard(
                mantraInput = state.newSessionMantraInput,
                onMantraChange = { viewModel.processIntent(HomeIntent.UpdateMantraInput(it)) },
                target = state.newSessionTargetInput,
                onChangeTargetClick = { viewModel.processIntent(HomeIntent.ShowCustomTargetDialog) },
                onStartClick = { viewModel.processIntent(HomeIntent.StartNewSession) }
            )
        }

        if (state.isCustomTargetDialogVisible) {
            CustomTargetDialog(
                currentValue = state.newSessionTargetInput,
                error = state.targetValidationError,
                onDismiss = { viewModel.processIntent(HomeIntent.HideCustomTargetDialog) },
                onSubmit = { viewModel.processIntent(HomeIntent.SubmitCustomTarget(it)) }
            )
        }
    }
}

@Composable
fun ResumeSessionCard(
    mantra: String?,
    currentCount: Int,
    targetCount: Int,
    onResumeClick: () -> Unit
) {
    NjCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Resume Session",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!mantra.isNullOrBlank()) {
                Text(text = "Mantra: $mantra", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(text = "Progress: $currentCount / $targetCount", color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(24.dp))
            NjButton(
                text = "Resume",
                onClick = onResumeClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NewSessionCard(
    mantraInput: String,
    onMantraChange: (String) -> Unit,
    target: String,
    onChangeTargetClick: () -> Unit,
    onStartClick: () -> Unit
) {
    NjCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "New Session",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = mantraInput,
                onValueChange = onMantraChange,
                label = { Text("Mantra (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Target: $target", color = MaterialTheme.colorScheme.onSurface)
                TextButton(onClick = onChangeTargetClick) {
                    Text("Change")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            NjButton(
                text = "Start Jaap",
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CustomTargetDialog(
    currentValue: String,
    error: String?,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var input by remember { mutableStateOf(currentValue) }

    NjDialog(
        title = "Set Target",
        onDismiss = onDismiss
    ) {
        Column {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = error != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                TextButton(onClick = { onSubmit(input) }) {
                    Text("Set")
                }
            }
        }
    }
}
