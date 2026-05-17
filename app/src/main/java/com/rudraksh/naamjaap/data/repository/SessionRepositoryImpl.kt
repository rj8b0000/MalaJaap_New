package com.rudraksh.naamjaap.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rudraksh.naamjaap.data.local.dao.SessionDao
import com.rudraksh.naamjaap.data.local.datastore.PreferencesKeys
import com.rudraksh.naamjaap.data.local.mapper.toActiveSession
import com.rudraksh.naamjaap.data.local.mapper.toDomain
import com.rudraksh.naamjaap.data.local.mapper.toEntity
import com.rudraksh.naamjaap.domain.model.ActiveSession
import com.rudraksh.naamjaap.domain.model.JapSession
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao,
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    override suspend fun getActiveSession(): ActiveSession? {
        val preferences = dataStore.data.first()
        return preferences.toActiveSession()
    }

    override suspend fun saveActiveSession(session: ActiveSession) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_ACTIVE_SESSION] = true
            
            if (session.mantra != null) {
                preferences[PreferencesKeys.ACTIVE_SESSION_MANTRA] = session.mantra
            } else {
                preferences.remove(PreferencesKeys.ACTIVE_SESSION_MANTRA)
            }
            
            preferences[PreferencesKeys.ACTIVE_SESSION_CURRENT_COUNT] = session.currentCount
            preferences[PreferencesKeys.ACTIVE_SESSION_TARGET_COUNT] = session.targetCount
            preferences[PreferencesKeys.ACTIVE_SESSION_ACTIVE_BEAD_INDEX] = session.activeBeadIndex
            preferences[PreferencesKeys.ACTIVE_SESSION_DIRECTION] = session.direction
            preferences[PreferencesKeys.ACTIVE_SESSION_GURU_BLOCKING_STATE] = session.guruBlockingState
            preferences[PreferencesKeys.ACTIVE_SESSION_ACCUMULATED_DURATION] = session.accumulatedActiveDuration
            preferences[PreferencesKeys.ACTIVE_SESSION_START_EPOCH] = session.sessionStartEpoch
            preferences[PreferencesKeys.ACTIVE_SESSION_IS_PAUSED] = session.isPaused
        }
    }

    override suspend fun clearActiveSession() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_ACTIVE_SESSION] = false
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_MANTRA)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_CURRENT_COUNT)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_TARGET_COUNT)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_ACTIVE_BEAD_INDEX)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_DIRECTION)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_GURU_BLOCKING_STATE)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_ACCUMULATED_DURATION)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_START_EPOCH)
            preferences.remove(PreferencesKeys.ACTIVE_SESSION_IS_PAUSED)
        }
    }

    override suspend fun saveSessionHistory(session: JapSession) {
        sessionDao.insertSession(session.toEntity())
    }

    override fun getAllSessions(): Flow<List<JapSession>> {
        return sessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
