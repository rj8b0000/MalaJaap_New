package com.rudraksh.naamjaap.domain.model

/**
 * Represents the transient state of an ongoing or paused Jap session.
 * This is meant to be persisted across process death but removed once the session completes.
 */
data class ActiveSession(
    val mantra: String? = null,
    val currentCount: Int = 0,
    val targetCount: Int = 108,
    val activeBeadIndex: Int = 0,
    val direction: Int = 1, // 1 for forward, -1 for backward
    val guruBlockingState: Boolean = false,
    val accumulatedActiveDuration: Long = 0L,
    val sessionStartEpoch: Long = System.currentTimeMillis(),
    val isPaused: Boolean = false
)
