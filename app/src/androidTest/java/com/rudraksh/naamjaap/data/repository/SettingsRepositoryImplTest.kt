package com.rudraksh.naamjaap.data.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rudraksh.naamjaap.domain.model.Settings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class SettingsRepositoryImplTest {

    private lateinit var settingsRepository: SettingsRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dataStore = PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("test_datastore_${Random.nextInt()}") }
        )
        settingsRepository = SettingsRepositoryImpl(dataStore)
    }

    @Test
    fun updateAndGetSettings() = runBlocking {
        val newSettings = Settings(
            hapticEnabled = false,
            soundEnabled = true,
            defaultTarget = 21
        )

        settingsRepository.updateSettings(newSettings)

        val retrievedSettings = settingsRepository.getSettings().first()
        assertFalse(retrievedSettings.hapticEnabled)
        assertEquals(true, retrievedSettings.soundEnabled)
        assertEquals(21, retrievedSettings.defaultTarget)
    }
}
