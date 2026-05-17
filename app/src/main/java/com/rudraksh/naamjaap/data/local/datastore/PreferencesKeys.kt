package com.rudraksh.naamjaap.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    // Global Settings
    val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val DEFAULT_TARGET = intPreferencesKey("default_target")

    // Active Session
    val HAS_ACTIVE_SESSION = booleanPreferencesKey("has_active_session")
    val ACTIVE_SESSION_MANTRA = stringPreferencesKey("active_session_mantra")
    val ACTIVE_SESSION_CURRENT_COUNT = intPreferencesKey("active_session_current_count")
    val ACTIVE_SESSION_TARGET_COUNT = intPreferencesKey("active_session_target_count")
    val ACTIVE_SESSION_ACTIVE_BEAD_INDEX = intPreferencesKey("active_session_active_bead_index")
    val ACTIVE_SESSION_DIRECTION = intPreferencesKey("active_session_direction")
    val ACTIVE_SESSION_GURU_BLOCKING_STATE = booleanPreferencesKey("active_session_guru_blocking_state")
    val ACTIVE_SESSION_ACCUMULATED_DURATION = longPreferencesKey("active_session_accumulated_duration")
    val ACTIVE_SESSION_START_EPOCH = longPreferencesKey("active_session_start_epoch")
    val ACTIVE_SESSION_IS_PAUSED = booleanPreferencesKey("active_session_is_paused")
}
