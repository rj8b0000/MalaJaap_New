package com.rudraksh.naamjaap.domain.model

/**
 * Represents the user's global preferences.
 */
data class Settings(
    val hapticEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val defaultTarget: Int = 108
)
