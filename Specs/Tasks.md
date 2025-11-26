# Project 3 Tasks: Character Creator Remastered

This document outlines the step-by-step tasks required to complete Project 3, focusing on migrating to MVVM architecture and adding character selection features.

## Phase 1: Setup & Dependencies
- [x] **Add Dependencies**
    - Open `app/build.gradle.kts`.
    - Add `implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")` (or latest compatible version) to the `dependencies` block.
    - Sync Gradle project.

## Phase 2: Data Layer & State Definition
- [x] **Review/Create Data Models**
    - Ensure `Character.kt` exists and is an immutable `data class` (Name, Class, Description, Stats).
    - Ensure `DataSource.kt` exists and contains `defaultCharacters` list.
- [x] **Create UI State**
    - Create a new file `CharacterCreatorUiState.kt`.
    - Define a `data class CharacterCreatorUiState`.
    - Add a property to hold the current `Character`.
    - (Optional) Add other UI-specific state flags if necessary (e.g., `isDropdownExpanded` - though often this is local UI state).

## Phase 3: ViewModel Implementation
- [x] **Create ViewModel Class**
    - Create `CharacterCreatorViewModel.kt`.
    - Extend `ViewModel`.
- [x] **Define StateFlow**
    - Create private `_uiState` as `MutableStateFlow<CharacterCreatorUiState>`.
    - Create public `uiState` as `StateFlow<CharacterCreatorUiState>` using `asStateFlow()`.
    - Initialize with a default character.
- [ ] **Implement Event Handlers (Business Logic)**
    - `onNameChange(newName: String)`: Update state with new name.
    - `onClassChange(newClass: String)`: Update state with new class.
    - `onDescriptionChange(newDesc: String)`: Update state with new description.
    - `updateStat(statName: String, delta: Int)`: Logic to increment/decrement stats, check point availability, and update derived stats (Attack/Defense).
    - `selectCharacter(character: Character)`: Replace the current character in state with a copy of the selected template.

## Phase 4: UI Refactoring (State Hoisting)
- [ ] **Refactor Composables to be Stateless**
    - Modify `TextEntry` (or equivalent) to accept `name`, `class`, `description` as parameters and `on...Change` lambdas. Remove internal state.
    - Modify `StatButtons` / `AttributeList` to accept current stats and `onStatUpdate` lambdas. Remove internal state.
- [ ] **Remove Local State**
    - Identify any `remember { mutableStateOf(...) }` in the main screen that manages character data and remove it (it now lives in the ViewModel).

## Phase 5: New UI Components
- [ ] **Implement Class Dropdown**
    - Create a composable `ClassDropdownMenu`.
    - Use `TextField` (read-only) with an `IconButton` or `ExposedDropdownMenuBox`.
    - Populate `DropdownMenu` items using `DataSource.defaultCharacters`.
    - Add "Custom" option to the list.
    - On selection, trigger a callback (e.g., `onCharacterSelected`).

## Phase 6: Integration
- [ ] **Connect ViewModel to Activity/Screen**
    - In `MainActivity.kt` (or your top-level Composable like `CharacterCreatorApp`):
        - Instantiate the ViewModel: `val viewModel: CharacterCreatorViewModel = viewModel()`.
        - Collect state: `val uiState by viewModel.uiState.collectAsState()`.
- [ ] **Pass Data & Events**
    - Pass `uiState.character` to child composables.
    - Pass method references (e.g., `viewModel::onNameChange`) or lambdas to child composables.

## Phase 7: Polish & Optional Features
- [ ] **Implement "Custom" Logic**
    - Ensure selecting "Custom" allows the user to edit the class name manually (or clears the class field).
    - Ensure selecting a template locks or auto-fills the class name.
- [ ] **Implement Base Stat Mins**
    - In `updateStat`, ensure a stat cannot be lowered below its starting value defined in the selected template.
- [ ] **Final Testing**
    - Verify rotation/configuration changes (ViewModel survives this).
    - Verify all inputs update the UI immediately.
    - Check for crashes.
