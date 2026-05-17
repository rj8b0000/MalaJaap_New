package com.rudraksh.naamjaap.domain.usecase

import com.rudraksh.naamjaap.domain.model.Settings
import com.rudraksh.naamjaap.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(settings: Settings) {
        settingsRepository.updateSettings(settings)
    }
}
