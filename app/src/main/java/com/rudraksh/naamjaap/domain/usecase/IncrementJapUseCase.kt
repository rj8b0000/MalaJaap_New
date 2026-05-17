package com.rudraksh.naamjaap.domain.usecase

import com.rudraksh.naamjaap.domain.model.ActiveSession
import javax.inject.Inject

class IncrementJapUseCase @Inject constructor() {

    companion object {
        const val MALA_BEAD_COUNT = 108
    }

    operator fun invoke(session: ActiveSession): ActiveSession {
        // If the session is paused, we shouldn't logically allow increments,
        // though UI should prevent it. If it happens, just return the same session.
        if (session.isPaused) return session

        var nextCount = session.currentCount
        var nextBeadIndex = session.activeBeadIndex
        var nextDirection = session.direction
        var hitGuruBead = false

        // Calculate the physical next bead based on current direction
        val attemptedNextBead = nextBeadIndex + nextDirection

        if (attemptedNextBead > MALA_BEAD_COUNT || attemptedNextBead < 1) {
            // Hit the Guru Bead!
            // 1. No increment to the logical Jap count.
            // 2. Automatically reverse the physical direction.
            nextDirection *= -1
            hitGuruBead = true
            
            // The active bead index remains the same (either 1 or 108),
            // waiting for the user's next swipe which will now be in the reversed direction.
        } else {
            // Normal valid bead increment
            nextCount++
            nextBeadIndex = attemptedNextBead
            hitGuruBead = false
        }

        return session.copy(
            currentCount = nextCount,
            activeBeadIndex = nextBeadIndex,
            direction = nextDirection,
            guruBlockingState = hitGuruBead
        )
    }
}
