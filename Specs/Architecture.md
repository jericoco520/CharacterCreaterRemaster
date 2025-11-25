# Architecture & Data Flow

## Data Models

### `Character.kt`
The core data structure.
```kotlin
data class Character(
    val name: String = "",
    val charClass: String = "",
    val description: String = "",
    val stats: Map<String, Int> = mapOf("Str" to 10, "Dex" to 10, ...),
    val pointsRemaining: Int = 20
)
```

### `CharacterCreatorUiState.kt`
The wrapper for screen state.
```kotlin
data class CharacterCreatorUiState(
    val character: Character = Character(),
    val isLoading: Boolean = false // Example of other UI state
)
```

## ViewModel Structure

### `CharacterCreatorViewModel.kt`
```kotlin
class CharacterCreatorViewModel : ViewModel() {
    // Backing property
    private val _uiState = MutableStateFlow(CharacterCreatorUiState())
    
    // Public exposed state
    val uiState: StateFlow<CharacterCreatorUiState> = _uiState.asStateFlow()

    // Actions
    fun onNameChange(name: String) { ... }
    fun selectCharacter(template: Character) { ... }
    fun updateStat(stat: String, delta: Int) { ... }
}
```

## View Hierarchy

```text
MainActivity
 └── CharacterCreatorApp (Screen Level)
      ├── Instantiates ViewModel
      ├── Collects uiState
      │
      ├── TextEntry (Stateless)
      │    ├── Name TextField
      │    ├── ClassDropdownMenu (New!)
      │    └── Description TextField
      │
      ├── StatButtons (Stateless)
      │    └── Row of +/- buttons
      │
      └── AttributeList (Stateless)
           └── Displays derived stats (Attack, Defense)
```

## Data Flow Diagram

1. **User Action**: User clicks "Warrior" in Dropdown.
2. **Event**: `ClassDropdownMenu` calls `onClassSelected("Warrior")` -> `MainActivity` calls `viewModel.selectCharacter(warriorTemplate)`.
3. **Update**: `ViewModel` updates `_uiState` with a new `Character` object copied from the template.
4. **Emission**: `uiState` emits the new state.
5. **Recomposition**: `MainActivity` observes the change, passes new `Character` to `TextEntry`.
6. **Render**: `TextEntry` redraws with "Warrior" pre-filled.
