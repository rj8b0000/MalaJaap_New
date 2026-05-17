package com.rudraksh.naamjaap.domain.usecase

import com.rudraksh.naamjaap.domain.model.JapSession
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionHistoryUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(): Flow<List<JapSession>> {
        return sessionRepository.getAllSessions()
    }
}
