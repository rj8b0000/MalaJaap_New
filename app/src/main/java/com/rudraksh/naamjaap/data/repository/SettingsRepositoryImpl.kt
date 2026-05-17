package com.rudraksh.naamjaap.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rudraksh.naamjaap.data.local.datastore.PreferencesKeys
import com.rudraksh.naamjaap.data.local.mapper.toSettings
import com.rudraksh.naamjaap.domain.model.Settings
import com.rudraksh.naamjaap.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override fun getSettings(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            preferences.toSettings()
        }
    }

    override suspend fun updateSettings(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTIC_ENABLED] = settings.hapticEnabled
            preferences[PreferencesKeys.SOUND_ENABLED] = settings.soundEnabled
            preferences[PreferencesKeys.DEFAULT_TARGET] = settings.defaultTarget
        }
    }
}
