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
- [ ] 4.8 **Verification Task:** Verify Room CRUD operations.
- [ ] 4.9 **Verification Task:** Verify DataStore persistence.

## Phase 5: Feature - Settings
- [ ] 5.1 Define `SettingsState`, `SettingsIntent`, `SettingsResult`, `SettingsEffect`.
- [ ] 5.2 Implement pure `SettingsReducer`.
- [ ] 5.3 Implement `SettingsViewModel`.
- [ ] 5.4 Build `SettingsScreen` UI and bind to ViewModel.

## Phase 6: Feature - Home
- [ ] 6.1 Define `HomeState`, `HomeIntent`, `HomeResult`, `HomeEffect`.
- [ ] 6.2 Implement pure `HomeReducer`.
- [ ] 6.3 Implement `HomeViewModel` (DataStore checks, loading last session).
- [ ] 6.4 Build `HomeScreen` UI.

## Phase 7: Feature - Active Session (Logic)
- [ ] 7.1 Define `SessionState`, `SessionIntent`, `SessionResult`, `SessionEffect`.
- [ ] 7.2 Implement pure `SessionReducer`.
- [ ] 7.3 Implement `SessionViewModel`: start session logic.
- [ ] 7.4 Implement `SessionViewModel`: pause/resume logic.
- [ ] 7.5 Implement `SessionViewModel`: completion detection logic.
- [ ] 7.6 Implement `SessionViewModel`: guru bead state transitions.
- [ ] 7.7 Implement `SessionViewModel`: timer lifecycle.
- [ ] 7.8 Implement `SessionViewModel`: persistence triggers.
- [ ] 7.9 Implement `SessionViewModel`: DataStore restoration flow.
- [ ] 7.10 Implement `SessionViewModel`: effect emission logic.
- [ ] 7.11 Implement lifecycle observer integration for app background persistence.
- [ ] 7.12 **Verification Task:** Verify process death restoration flow.

## Phase 8: Feature - Active Session (UI & Mala Rendering)
- [ ] 8.1 Implement Mala UI: wheel state model.
- [ ] 8.2 Implement Mala UI: virtual index math logic.
- [ ] 8.3 Implement Mala UI: bead renderer.
- [ ] 8.4 Implement Mala UI: active bead highlight logic.
- [ ] 8.5 Implement Mala UI: guru bead renderer.
- [ ] 8.6 Implement Mala UI: gesture threshold logic.
- [ ] 8.7 Implement Mala UI: snap animation.
- [ ] 8.8 Implement Mala UI: direction detection.
- [ ] 8.9 Implement Mala UI: SessionScreen integration.
- [ ] 8.10 Implement HUD for large formatted numbers and timer display.
- [ ] 8.11 Bind `SessionScreen` to `SessionViewModel`, handle `Effect` collection for haptics/audio.
- [ ] 8.12 **Verification Task:** Verify gesture invariants (exactly 1 gesture = 1 increment).
- [ ] 8.13 **Verification Task:** Verify render performance at high counts.

## Phase 9: Feature - History & Completion
- [ ] 9.1 Build `SessionCompleteScreen` UI.
- [ ] 9.2 Define `HistoryState`, `HistoryIntent`, `HistoryResult`.
- [ ] 9.3 Implement `HistoryReducer`.
- [ ] 9.4 Implement `HistoryViewModel`.
- [ ] 9.5 Build `HistoryScreen` UI.

## Phase 10: Navigation & Integration
- [ ] 10.1 Setup Jetpack Navigation Compose `NavHost` with lowercase string routes.
- [ ] 10.2 Wire up all screens to the `NavHost`.
- [ ] 10.3 **Verification Task:** Verify comprehensive accessibility requirements (touch targets, contrast, scalable text).
