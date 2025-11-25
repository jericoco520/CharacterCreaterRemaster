# Background & Best Practices

## MVVM Architecture (Model-View-ViewModel)
This project transitions from a monolithic UI approach to MVVM.
- **Model**: Your data layer (`Character`, `DataSource`). It knows nothing about the UI.
- **View**: Your Composables (`MainActivity`, `TextEntry`, etc.). They simply render the state they are given and emit events when user interacts. They should not contain business logic.
- **ViewModel**: The bridge. It holds the `UiState`, processes business logic (like calculating remaining points), and exposes data via `StateFlow`. It survives configuration changes (like screen rotation).

## State Management
### StateFlow vs MutableState
- **MutableState (`mutableStateOf`)**: Great for local UI state (like "is this dropdown expanded?").
- **StateFlow**: Best for ViewModel state. It is a flow that emits updates to the UI.
- **collectAsState()**: In Compose, we collect the `StateFlow` from the ViewModel and convert it into a Compose `State` so the UI recomposes whenever the data changes.

### Immutability
In Kotlin and Compose, data classes should ideally be immutable (`val` properties).
- When you need to change a property, use `.copy()`.
- **Why?** Compose skips recomposition if the object reference hasn't changed. If you mutate a property inside an object, Compose might not know it needs to redraw. Creating a new copy guarantees the UI updates.

Example:
```kotlin
// Don't do this
currentState.character.name = "New Name" 

// Do this
_uiState.update { it.copy(character = it.character.copy(name = "New Name")) }
```

## Unidirectional Data Flow (UDF)
1. **State flows down**: ViewModel -> UI.
2. **Events flow up**: UI -> ViewModel.
The UI never modifies the state directly. It asks the ViewModel to do it.

## Dropdown Menus in Compose
There are two main ways to do dropdowns:
1. **Standard `DropdownMenu`**: Attached to a Box or Column, triggered by a button.
2. **`ExposedDropdownMenuBox`**: Material Design component that combines a TextField and a Menu. This is often cleaner for "Select an option" inputs.

## Common Pitfalls
- **Logic in Composables**: Avoid calculating "Remaining Points" inside the Composable function. Do it in the ViewModel or the Data Class.
- **Hardcoding Strings**: Use `stringResource(R.string.name)` where possible, though for this project hardcoded strings are often acceptable for simplicity.
- **State Reset on Rotation**: If you use `remember { mutableStateOf() }` inside a Composable for data that should persist (like the character name), it will be lost on rotation unless you use `rememberSaveable`. However, moving this state to the **ViewModel** automatically solves this problem because ViewModels survive rotation.
