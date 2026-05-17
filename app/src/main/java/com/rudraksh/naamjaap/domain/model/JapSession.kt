package com.rudraksh.naamjaap.domain.model

/**
 * Represents a historical record of a Jap session.
 * Used for persisting completed or cancelled sessions to the database.
 */
data class JapSession(
    val id: Long = 0,
    val targetCount: Int,
    val completedCount: Int,
    val mantra: String? = null,
    val durationMillis: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean
)
