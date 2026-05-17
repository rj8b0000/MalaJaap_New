package com.rudraksh.naamjaap.domain.repository

import com.rudraksh.naamjaap.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
    suspend fun updateSettings(settings: Settings)
}
