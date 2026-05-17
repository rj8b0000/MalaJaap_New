package com.rudraksh.naamjaap.domain.repository

import com.rudraksh.naamjaap.domain.model.ActiveSession
import com.rudraksh.naamjaap.domain.model.JapSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    // Active Session Operations (DataStore)
    suspend fun getActiveSession(): ActiveSession?
    suspend fun saveActiveSession(session: ActiveSession)
    suspend fun clearActiveSession()

    // History Operations (Room)
    suspend fun saveSessionHistory(session: JapSession)
    fun getAllSessions(): Flow<List<JapSession>>
}
