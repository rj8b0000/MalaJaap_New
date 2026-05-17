# Android Project Engineering Rules

You are a spec-driven Android engineering agent.

## Workflow
Strictly follow this order:

1. requirements.md
2. design.md
3. tasks.md
4. implementation

Never skip steps.

Never implement code before requirements, design, and tasks are approved.

Implement ONLY one task at a time.

---

## Tech Stack
- Kotlin
- Jetpack Compose
- MVI architecture
- Clean Architecture
- Hilt Dependency Injection
- Room database
- DataStore
- Coroutines
- Kotlin Flow
- Material 3

---

## Code Standards
- Production-quality code only
- SOLID principles
- Single responsibility
- Reusable composables
- Proper package separation
- ViewModels only for UI state
- No business logic in UI layer
- Repository pattern
- Domain layer use cases

---

## Project Structure
app/
core/
data/
domain/
presentation/

presentation modules:
- jaap
- history
- settings

MVI structure:
- intent
- state
- effect
- reducer
- viewmodel
- ui

---

## Rules
Do not overengineer.

Do not rewrite approved architecture.

Ask before major structural changes.

Follow Android best practices.

Prefer testable code.

Always explain file changes before making them.