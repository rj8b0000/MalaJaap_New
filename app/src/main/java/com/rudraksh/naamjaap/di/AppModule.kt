package com.rudraksh.naamjaap.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Application-wide dependencies (like CoroutineDispatchers) will be provided here
}
