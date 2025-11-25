# Project 2: Character Creator - Specs

This document outlines the specifications for the Character Creator project.

## 1. High-Level Goals

- **Build a Single-Screen Android App:** Create a character creation screen using Jetpack Compose.
- **Reactive UI:** The user interface must update in real-time as the user enters information and adjusts character stats.
- **State Management:** Implement state management within Composables using `remember` and state hoisting with callbacks.
- **Reusable Components:** Develop modular and reusable Composable functions for different parts of the UI.
- **Data Integration:** Connect the UI to the provided `Character.kt` data model.

## 2. Core Features & Tasks

### 2.1. Character Information Section

-   [x] Implement a `TextField` for the character's **Name**.
-   [x] Implement a `TextField` for the character's **Class/Type**.
-   [x] Implement a multi-line `TextField` for the character's **Description**.
-   [x] Ensure user input in these fields updates the `Character` state object reactively.

### 2.2. Stat Allocation Section

-   [x] Create controls (e.g., `Button`s with "+" and "-") for four stats: **Power**, **Endurance**, **Speed**, and **Focus**.
-   [x] A total of **10 points** can be distributed among the stats.
-   [x] The increment buttons should be disabled or have no effect when all points are used.
-   [x] The decrement buttons should be disabled or have no effect when a stat's value is 0.
-   [x] Update the `Character` state using the provided `updateStat` function when buttons are clicked.

### 2.3. Points Display

-   [x] Display the **remaining stat points** available for allocation.
-   [x] This display must update live as the user allocates or de-allocates points.

### 2.4. Derived Attributes Display

-   [x] Display the character's derived attributes: **Attack**, **Defense**, and **Cost**.
-   [x] These values must update live as the character's stats change.

## 3. Composable Architecture

The UI should be broken down into the following (or similar) reusable composable functions:

-   **`CharacterCreatorApp` (Top-Level Composable):**
    -   Manages the overall layout and holds the character state.
    -   Contains all other composables.

-   **`CharacterInfoInput(name, class, description, onNameChange, ...)`:**
    -   A composable function responsible for the text input fields (Name, Class, Description).
    -   Uses state hoisting to report value changes to the parent.

-   **`StatControl(statName, statValue, onIncrement, onDecrement)`:**
    -   A composable for a single stat, including its name, value, and increment/decrement buttons.
    -   Hoists click events up to `CharacterCreatorApp`.

-   **`StatGrid` or `StatList`:**
    -   A layout composable (e.g., using `Column` or `LazyVerticalGrid`) that arranges the four `StatControl` composables.

-   **`DerivedAttributesDisplay(character)`:**
    -   A composable that takes the character object and displays the `Attack`, `Defense`, and `Cost` attributes.

## 4. Data Model (`Character.kt`)

-   The `Character` data class is immutable. Updates must be performed by creating a new instance using the `.copy()` method.
-   State should be held in a `MutableState<Character>` created via `rememberCharacterState()`.
-   Stat updates must use the provided `updateStat()` helper function to ensure business logic (point limits, attribute recalculation) is correctly applied.

## 5. Layout and Design

-   Use `Column`, `Row`, and `Spacer` to create a clean, organized, and visually appealing layout.
-   The main layout should be vertically arranged.
-   Ensure adequate spacing between UI elements.
-   (Optional) Use `Icon`s and `MaterialTheme` to enhance the visual design.
