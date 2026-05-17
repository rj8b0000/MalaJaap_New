package com.rudraksh.naamjaap.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.rudraksh.naamjaap.data.local.NaamJaapDatabase
import com.rudraksh.naamjaap.data.local.dao.SessionDao
import com.rudraksh.naamjaap.data.repository.SessionRepositoryImpl
import com.rudraksh.naamjaap.data.repository.SettingsRepositoryImpl
import com.rudraksh.naamjaap.domain.repository.SessionRepository
import com.rudraksh.naamjaap.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "naamjaap_preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideNaamJaapDatabase(@ApplicationContext context: Context): NaamJaapDatabase {
        return Room.databaseBuilder(
            context,
            NaamJaapDatabase::class.java,
            NaamJaapDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: NaamJaapDatabase): SessionDao {
        return database.sessionDao
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
