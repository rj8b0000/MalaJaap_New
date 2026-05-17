package com.rudraksh.naamjaap.core.mvi

/**
 * Base interface for all UI states.
 * Represents the immutable state of a single screen/feature.
 */
interface MviState

/**
 * Base interface for all user intents (actions).
 * Represents actions triggered by the user from the UI.
 */
interface MviIntent

/**
 * Base interface for the results of use cases.
 * Represents the internal outcome of business logic.
 */
interface MviResult

/**
 * Base interface for one-off side effects.
 * Examples: Navigation, showing a Toast, playing haptics/audio.
 */
interface MviEffect

/**
 * Pure function interface that derives a new state from the current state and a result.
 */
interface Reducer<S : MviState, R : MviResult> {
    fun reduce(state: S, result: R): S
}
