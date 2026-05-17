# Naam Jaap - System Design Document

## 1. Architectural Overview
The application strictly follows **Clean Architecture** combined with **MVI (Model-View-Intent)** in the presentation layer.
Dependency Injection is handled by **Hilt**.
Asynchronous operations and state streams are managed via **Kotlin Coroutines and Flow**.

### 1.1 Layers
1. **Presentation Layer**: UI (Jetpack Compose), ViewModels, MVI State/Intent/Effect/Result/Reducer. Owns all UI state, gesture logic, animations, physical bead positioning, and execution of hardware effects (haptics/audio).
2. **Domain Layer**: Pure Kotlin. Use Cases, Repository Interfaces, Domain Models. Contains pure business rules (e.g., target completion checks, validation). **Must NOT** own or know about physical UI bead positioning or visual rendering logic.
3. **Data Layer**: Repository Implementations, Room Database (Local Storage), DataStore (Preferences and active session persistence).

## 2. Presentation Layer (MVI & Navigation)

### 2.1 Core MVI Components
- **State**: A single immutable data class representing the UI state for a specific feature.
- **Intent**: Sealed class representing user actions.
- **Result Contracts (Feature-Scoped)**: Sealed classes representing the internal outcome of domain use cases (`HomeResult`, `SessionResult`, `HistoryResult`, `SettingsResult`).
- **Reducer**: A strictly **pure function** `(State, Result) -> State`. It unambiguously derives the next state solely from the current state and the incoming feature-scoped Result.
- **Effect**: Sealed class for one-off events (e.g., `NavigateToHistory`, `PlayHapticFeedback`).
- **ViewModel Flow**: The ViewModel orchestrates the flow: `Intent` -> invokes Use Case -> produces `Result` -> feeds `Reducer` -> updates `State`.

### 2.2 Navigation Compose Architecture
Navigation is handled by Jetpack Navigation Compose with a single `NavHost`.
**Routes (Lowercase Constants):**
- `home`: Entry point.
- `session`: The active Jap interface.
- `session_complete`: Summary shown upon reaching the target.
- `history`: List of past sessions.
- `settings`: User preferences.

### 2.3 Shared UI Component Architecture
Reusable Compose components will be built in the `presentation/common/components` package to ensure consistency:
- `NjButton`: Standard stylized button (primary, secondary, text variants).
- `NjTopAppBar`: Consistent app bar with back navigation and title.
- `NjDialog`: Standardized alert/confirmation dialogs.
- `NjCard`: Standard surface with defined elevation and border properties.

## 3. Data Layer Design

### 3.1 Room Database (History)
**Entity: `SessionEntity`**
- `id`: Long (Primary Key, Auto-generate)
- `targetCount`: Int (Logical target chosen by user)
- `completedCount`: Int (Actual count achieved)
- `mantra`: String? (Nullable)
- `durationMillis`: Long (Active time spent, excluding pauses)
- `timestamp`: Long (Epoch time of session completion/save)
- `isCompleted`: Boolean

### 3.2 DataStore (Settings & Active Session)
Active session persistence **must** use DataStore exclusively.

**Preferences:**
- `hapticEnabled`: Boolean (Default: true)
- `soundEnabled`: Boolean (Default: true)
- `defaultTarget`: Int (Default: 108)

**Active Session State Recovery:**
To survive process death and enable session resumption, the exact state is flushed to DataStore:
- `activeSession_mantra`: String?
- `activeSession_currentCount`: Int
- `activeSession_targetCount`: Int
- `activeSession_activeBeadIndex`: Int
- `activeSession_direction`: Int (e.g., 1 for forward, -1 for backward)
- `activeSession_guruBlockingState`: Boolean
- `activeSession_accumulatedActiveDuration`: Long
- `activeSession_sessionStartEpoch`: Long
- `activeSession_isPaused`: Boolean

**Persistence Triggers:** Active session state must be saved to DataStore:
1. After every valid bead increment.
2. On session pause.
3. On app backgrounding / lifecycle pause.
4. On session end.

## 4. Domain Layer

### 4.1 Use Cases
- `StartSessionUseCase`
- `IncrementJapUseCase`
- `ValidateCustomTargetUseCase` (Enforces validation: minimum 1, maximum 100000, numeric only).
- `SaveSessionUseCase`
- `GetSessionHistoryUseCase`
- `GetSettingsUseCase`
- `UpdateSettingsUseCase`

### 4.2 Repository Interfaces
- `SessionRepository`: Handles saving and retrieving history and DataStore active session state.
- `SettingsRepository`: Handles preferences via DataStore.

## 5. UI & UX Design System

### 5.1 Theming & Colors
- **Theme**: **Dark-only immersive theme for v1**. No light mode support.
- **Background**: Deep rich dark colors (e.g., `#0F0F0F`, `#1A1A1A`).
- **Accents**: Premium Gold/Brass (`#D4AF37`, `#FFDF00`).
- **Typography**: Clean, sans-serif or elegant serif for numbers/mantras. High contrast.

### 5.2 Mala Virtualized Rendering & Guru Bead Strategy
- **Independence**: Logical target count is independent of the physical 108+1 bead mala.
- **Rendering Mechanism**:
  - Implement discrete vertical swipe gestures (similar to reels) using a custom pointer input gesture tracker.
  - Swipe up moves the mala one bead forward; swipe down moves it one bead backward.
  - Snap animations explicitly lock out further gesture input until completed.
  - Strict mapping: 1 discrete swipe gesture = exactly 1 bead snapped = exactly 1 increment intent. Fling and long drags are completely ignored.
- **Guru Bead Automatic Reversal Strategy**:
  1. **No increment**: Reaching the Guru bead does not increase the Jap count.
  2. **Block progression**: It acts as a hard physical barrier.
  3. **Automatic Reversal**: Upon hitting the Guru bead, an explanation effect/toast is briefly shown, and the visual direction automatically reverses.
  4. **Resume**: The user can immediately resume counting by swiping in the newly reversed physical direction.

### 5.3 UX & Accessibility Requirements
- **Count HUD**: The logical count display must support formatted large numbers (e.g., "1,008", "100,000") cleanly without breaking layout.
- **Touch Targets**: Minimum `48dp` standard for all interactive UI elements.
- **Contrast**: Ensure highly readable contrast ratios for all text against the dark theme background.
- **Scalable Text**: Support dynamic font sizes/scaling based on OS accessibility settings without truncating vital text.
- **Reduced Haptic Support**: Respect system-level reduced haptics accessibility settings if active.

### 5.4 Session Resumption Behavior
If a user closes the app with an active session, the `home` screen will read the DataStore. It will present a primary "Resume Session" card **only if** a session exists **and** is not marked as completed.

## 6. Directory Structure
```
app/src/main/java/com/rudraksh/naamjaap/
├── core/
│   ├── theme/           # Compose dark theme, colors, typography
│   ├── navigation/      # NavHost, Routes, Destinations
│   ├── mvi/             # Base pure MVI components
│   └── util/            # Extensions, Constants
├── domain/
│   ├── model/           # Domain models
│   ├── repository/      # Interfaces
│   └── usecase/         # Interactors (pure business logic, stateless)
├── data/
│   ├── local/           # Room Database, DAOs, DataStore
│   └── repository/      # Implementations
├── presentation/
│   ├── common/          # Shared UI components (NjButton, etc.)
│   ├── home/            # Home screen, MVI components
│   ├── session/         # Active session, Mala UI, MVI components
│   ├── history/         # History screen, MVI components
│   └── settings/        # Settings screen, MVI components
└── di/                  # Hilt Modules
```

## 7. Dependency Architecture & Execution
- **Version Management**: Gradle Version Catalogs (`libs.versions.toml`).
- **Dependency Injection Rules**:
  - `@Singleton` for Repositories and DataStore/Room instances.
  - **Use Cases must be unscoped and stateless**, instantiated directly or provided without a lifecycle scope.
- **Hardware Effects Handling**: Audio and haptics belong strictly to the UI layer via one-off `Effect` observation, decoupling ViewModels from Android APIs.
- **State Management**: `StateFlow` and `SharedFlow` exposed from ViewModels collected safely using `collectAsStateWithLifecycle()`.
