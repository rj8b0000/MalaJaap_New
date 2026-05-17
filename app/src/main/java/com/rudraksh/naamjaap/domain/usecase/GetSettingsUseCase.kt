package com.rudraksh.naamjaap.domain.usecase

import com.rudraksh.naamjaap.domain.model.Settings
import com.rudraksh.naamjaap.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings> {
        return settingsRepository.getSettings()
    }
}
