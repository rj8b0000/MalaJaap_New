package com.rudraksh.naamjaap.data.local.mapper

import androidx.datastore.preferences.core.Preferences
import com.rudraksh.naamjaap.data.local.datastore.PreferencesKeys
import com.rudraksh.naamjaap.domain.model.ActiveSession
import com.rudraksh.naamjaap.domain.model.Settings

fun Preferences.toSettings(): Settings {
    return Settings(
        hapticEnabled = this[PreferencesKeys.HAPTIC_ENABLED] ?: true,
        soundEnabled = this[PreferencesKeys.SOUND_ENABLED] ?: true,
        defaultTarget = this[PreferencesKeys.DEFAULT_TARGET] ?: 108
    )
}

fun Preferences.toActiveSession(): ActiveSession? {
    val hasSession = this[PreferencesKeys.HAS_ACTIVE_SESSION] ?: false
    if (!hasSession) return null

    return ActiveSession(
        mantra = this[PreferencesKeys.ACTIVE_SESSION_MANTRA],
        currentCount = this[PreferencesKeys.ACTIVE_SESSION_CURRENT_COUNT] ?: 0,
        targetCount = this[PreferencesKeys.ACTIVE_SESSION_TARGET_COUNT] ?: 108,
        activeBeadIndex = this[PreferencesKeys.ACTIVE_SESSION_ACTIVE_BEAD_INDEX] ?: 1,
        direction = this[PreferencesKeys.ACTIVE_SESSION_DIRECTION] ?: 1,
        guruBlockingState = this[PreferencesKeys.ACTIVE_SESSION_GURU_BLOCKING_STATE] ?: false,
        accumulatedActiveDuration = this[PreferencesKeys.ACTIVE_SESSION_ACCUMULATED_DURATION] ?: 0L,
        sessionStartEpoch = this[PreferencesKeys.ACTIVE_SESSION_START_EPOCH] ?: System.currentTimeMillis(),
        isPaused = this[PreferencesKeys.ACTIVE_SESSION_IS_PAUSED] ?: false
    )
}
