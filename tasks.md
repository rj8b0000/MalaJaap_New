# Naam Jaap - Implementation Tasks

## Phase 1: Project & Architecture Setup
- [x] 1.1 Configure `libs.versions.toml` with dependencies.
- [x] 1.2 Setup `build.gradle.kts` files and apply plugins.
- [x] 1.3 Create base package structure (`core`, `domain`, `data`, `presentation`, `di`).
- [x] 1.4 Implement Base MVI interfaces (`State`, `Intent`, `Result`, `Effect`, `Reducer`).
- [x] 1.5 Setup Hilt Application class and App Module.

## Phase 2: Core Theme & Shared Components
- [x] 2.1 Define Color tokens, Typography, and Shapes for the Dark theme.
- [x] 2.2 Create `NjButton` component.
- [x] 2.3 Create `NjTopAppBar` component.
- [x] 2.4 Create `NjCard` component.
- [x] 2.5 Create `NjDialog` component.

## Phase 3: Domain Layer Implementation
- [x] 3.1 Define pure Domain Models (`Session`, `Settings`).
- [x] 3.2 Define Repository Interfaces (`SessionRepository`, `SettingsRepository`).
- [x] 3.3 Implement `ValidateCustomTargetUseCase` (min 1, max 100000).
- [x] 3.4 Implement `IncrementJapUseCase` (handles logical limits).
- [x] 3.5 Implement `StartSessionUseCase`.
- [x] 3.6 Implement `SaveSessionUseCase`.
- [x] 3.7 Implement `GetSessionHistoryUseCase`.
- [x] 3.8 Implement `GetSettingsUseCase`.
- [x] 3.9 Implement `UpdateSettingsUseCase`.

## Phase 4: Data Layer Implementation
- [x] 4.1 Setup Room Database and `SessionDao`.
- [x] 4.2 Define `SessionEntity` and implement Entity ↔ Domain mappers.
- [x] 4.3 Setup Preferences DataStore (`Settings` and `ActiveSession` keys).
- [x] 4.4 Implement DataStore ↔ Domain mappers.
- [x] 4.5 Implement `SettingsRepositoryImpl`.
- [x] 4.6 Implement `SessionRepositoryImpl` (Room and DataStore integration).
- [x] 4.7 Provide Data layer dependencies via Hilt module.
- [x] 4.8 **Verification Task:** Verify Room CRUD operations.
- [x] 4.9 **Verification Task:** Verify DataStore persistence.

## Phase 5: Feature - Settings
- [x] 5.1 Define `SettingsState`, `SettingsIntent`, `SettingsResult`, `SettingsEffect`.
- [x] 5.2 Implement pure `SettingsReducer`.
- [x] 5.3 Implement `SettingsViewModel`.
- [x] 5.4 Build `SettingsScreen` UI and bind to ViewModel.

## Phase 6: Feature - Home
- [x] 6.1 Define `HomeState`, `HomeIntent`, `HomeResult`, `HomeEffect`.
- [x] 6.2 Implement pure `HomeReducer`.
- [x] 6.3 Implement `HomeViewModel` (DataStore checks, loading last session).
- [x] 6.4 Build `HomeScreen` UI.

## Phase 7: Feature - Active Session (Logic)
- [x] 7.1 Define `SessionState`, `SessionIntent`, `SessionResult`, `SessionEffect`.
- [x] 7.2 Implement pure `SessionReducer`.
- [x] 7.3 Implement `SessionViewModel`: start session logic.
- [x] 7.4 Implement `SessionViewModel`: pause/resume logic.
- [x] 7.5 Implement `SessionViewModel`: completion detection logic.
- [x] 7.6 Implement `SessionViewModel`: guru bead state transitions.
- [x] 7.7 Implement `SessionViewModel`: timer lifecycle.
- [x] 7.8 Implement `SessionViewModel`: persistence triggers.
- [x] 7.9 Implement `SessionViewModel`: DataStore restoration flow.
- [x] 7.10 Implement `SessionViewModel`: effect emission logic.
- [x] 7.11 Implement lifecycle observer integration for app background persistence.
- [x] 7.12 **Verification Task:** Verify process death restoration flow.

## Phase 8: Feature - Active Session (UI & Mala Rendering)
- [x] 8.1 Implement Mala UI: wheel state model.
- [x] 8.2 Implement Mala UI: virtual index math logic.
- [x] 8.3 Implement Mala UI: bead renderer.
- [x] 8.4 Implement Mala UI: active bead highlight logic.
- [x] 8.5 Implement Mala UI: guru bead renderer.
- [x] 8.6 Implement Mala UI: gesture threshold logic.
- [x] 8.7 Implement Mala UI: snap animation.
- [x] 8.8 Implement Mala UI: direction detection.
- [x] 8.9 Implement Mala UI: SessionScreen integration.
- [x] 8.10 Implement HUD for large formatted numbers and timer display.
- [x] 8.11 Bind `SessionScreen` to `SessionViewModel`, handle `Effect` collection for haptics/audio.
- [x] 8.12 **Verification Task:** Verify gesture invariants (exactly 1 gesture = 1 increment).
- [x] 8.13 **Verification Task:** Verify render performance at high counts.

## Phase 9: Feature - History & Completion
- [x] 9.1 Build `SessionCompleteScreen` UI.
- [x] 9.2 Define `HistoryState`, `HistoryIntent`, `HistoryResult`.
- [x] 9.3 Implement `HistoryReducer`.
- [x] 9.4 Implement `HistoryViewModel`.
- [x] 9.5 Build `HistoryScreen` UI.

## Phase 10: Navigation & Integration
- [x] 10.1 Setup Jetpack Navigation Compose `NavHost` with lowercase string routes.
- [x] 10.2 Wire up all screens to the `NavHost`.
- [x] 10.3 **Verification Task:** Verify comprehensive accessibility requirements (touch targets, contrast, scalable text).
