# Naam Jaap - Product Requirements Document (PRD)

## 1. Product Vision & Goal
Build a premium, immersive spiritual meditation and mantra counting Android app. The core experience must be tactile, sacred, calm, minimal, and highly responsive. It should simulate the physical interaction of a real mala (prayer beads).

## 2. Functional Requirements
### 2.1. Session Management
- **Start Session**: Users can initiate a new Jap (chanting) session.
- **Target Selection**: Users must select a logical target count before starting. Presets: 11, 21, 51, 108, 1008, and Custom. The target count is independent from the visual physical mala representation.
- **Custom Target Validation**: Custom targets must have a defined minimum (e.g., 1) and maximum (e.g., 100,000) value with validation to prevent overflow or invalid states.
- **Mantra Input**: Optional text input to define the specific mantra being chanted.
- **Session Controls**: Ability to Pause, Resume, and End a session at any time.
- **Completion Tracking**: Real-time tracking of current logical count vs. target count.
- **Timer**: Background timer tracking the active duration of the session. The timer must strictly exclude any paused duration.

### 2.2. History & Statistics
- **Past Sessions Logging**: Save all sessions, explicitly including both completed and incomplete (partially completed) sessions.
- **Session Details**: Each history entry must include:
  - Completion state (e.g., 108/108 or 45/108).
  - Total active duration of the session.
  - Mantra summary (if entered).
  - Date and time of the session.

### 2.3. Settings & Preferences
- **Haptic Feedback**: Toggle vibration on/off for bead interactions.
- **Audio Feedback**: Toggle bead click sounds on/off.
- **Theming**: Selection between available themes (focusing on dark, premium, spiritual aesthetics).
- **Default Target**: Ability to set a default target count for quicker session starts.

## 3. UX, Visual & Navigation Requirements
- **Theme & Atmosphere**: Dark, immersive spiritual theme with premium golden accents. Avoid generic, bright, or flat designs.
- **Visual Fidelity**: Realistic mala aesthetics with depth, shadow, and material feel. The visual physical mala always consists of exactly 108 regular beads plus 1 Guru bead, regardless of the user's logical target count.
- **Motion & Animation**: Fluid, subtle, and stable animations. No abrupt transitions or clunky UI elements.
- **Accessibility Requirements**: High contrast ratios for text. Support for TalkBack and screen readers. Meaningful content descriptions for interactive elements (like the mala bead swipe area, pause/play buttons). Sufficient touch target sizes.
- **Navigation Requirements**: 
  - Smooth, predictable transitions between screens (e.g., slide or fade animations).
  - Clear back navigation (system back button and on-screen back arrows) without losing active session state accidentally.
  - No deep nested navigation trees to keep the app minimal.
- **Screen Flow**:
  1. **Home**: Minimalistic entry point to configure and launch a session. Must display a summary of the *last session* (e.g., "Last session: 108 counts of Om on [Date]").
  2. **Active Session**: Distraction-free, immersive mala interface.
  3. **Session Complete**: Rewarding, calm summary screen.
  4. **History**: Elegant list of past sessions.
  5. **Settings**: Simple, accessible preference toggles.

## 4. Gesture Interaction Rules
- **Interaction Axis**: Strictly vertical scrolling/swiping for the mala.
- **Single Intent Rule**: One distinct bead movement per intentional gesture. Continuous fast swiping should not uncontrollably spin the mala.
- **Increment Trigger**: One successful bead snap/movement equals exactly one Jap increment.
- **Tactility**: Snapping behavior must be smooth, naturally settling the next bead into the active center position.
- **Feedback Loop**: Every successful increment must immediately trigger the chosen haptic and/or audio feedback.

## 5. Guru Bead Behavior Definition
- **Physical Count**: The mala visually consists of exactly 108 beads + 1 Guru bead (Sumeru).
- **Traversal Rule**: The user must *never* cross the Guru bead. It acts as a physical barrier in the interaction.
- **No Increment on Guru Bead**: Reaching or touching the Guru bead does *not* increment the jap count.
- **Reaching the Guru Bead**: Upon completing 108 counts (reaching the Guru bead), the visual mala must reverse or flip, indicating that the user is starting the next cycle going back the other way.
- **Visual Indication**: The Guru bead must be visually distinct from standard beads (e.g., larger, different texture, or distinct tassel).

## 6. Technical & Performance Constraints
- **Platform**: Android Min SDK 26, Target SDK 34.
- **Tech Stack**: Kotlin, Jetpack Compose, Clean Architecture, MVI, Hilt, Room, DataStore, Coroutines/Flow.
- **Performance & Virtualization**:
  - The mala UI must render and animate smoothly at 60fps+ (or 120fps on supported displays).
  - **Virtualized Bead Rendering**: Visual rendering of the beads must be completely independent of the logical target count. The UI should only render the visible beads on screen (plus a small buffer) wrapping logically within the 108 physical bead constraints, ensuring zero performance drop even if the logical target is 100,000.
- **Persistence**: Offline-first architecture. Room for history data; DataStore for preferences.

## 7. Edge Cases
- **Active Session Survivability**: The active session (including count, elapsed time, and state) must survive process death, system initiated background kills, and device orientation changes.
- **Sound/Haptic Failures**: Any failure in playing audio clicks or triggering vibration must fail silently and *not* break the application flow or interrupt the counting logic.
- **Interruption**: Incoming calls or alarms should auto-pause the session if possible, or at minimum not lose state.
- **Accidental Swipes**: The gesture threshold must be tuned to prevent accidental micro-touches from registering as a count.
- **Storage**: Graceful handling if device storage is completely full when saving history.

## 8. Acceptance Criteria
- [ ] MVI architecture strictly followed for all screens.
- [ ] No business logic present in the UI layer.
- [ ] Mala interaction feels physical, precise (1 gesture = 1 count), and performant.
- [ ] Guru bead rules are strictly enforced: fixed 108 count, no crossing, no jap increment.
- [ ] Haptic and audio feedback sync perfectly with the visual bead snap, and fail gracefully if unavailable.
- [ ] Sessions survive process death and app backgrounding.
- [ ] Session timer explicitly pauses when the session is paused.
- [ ] History correctly records all completed and incomplete session data offline.
- [ ] Custom targets enforce proper min/max validation.
- [ ] Home screen correctly shows the last session summary.
- [ ] App meets defined accessibility and navigation requirements.
- [ ] UI strictly matches the premium, dark, immersive aesthetic requirement.
