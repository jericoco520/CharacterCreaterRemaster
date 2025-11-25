# Project 03 - Character Creator Remastered (ViewModel & Class Selection)

![App Screenshot](img/screenshot_final.png)

## Overview
In **Project 3**, you’ll continue building on your **Character Creator** app from Project 2.  
Previously, you managed all the UI and state directly inside composables.  
Now, you’ll separate your logic and data from the UI by using a **ViewModel** and a **UiState** class.  

You’ll also add a **dropdown menu** that lets you select from pre-made character templates (loaded from a `DataSource`), and the UI will automatically update to show that character’s information.

This project focuses on **state management**, **ViewModel architecture**, and **reactive UI updates**.

---

## Setup – Add ViewModel Dependencies

In your `app/build.gradle.kts`, make sure the following dependency is added:

```kotlin
dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
}
```

Sync Gradle after adding the dependency.

---

## Character Data Overview

You’ll now use three main files for your data and logic:

| File | Purpose |
|------|----------|
| `Character.kt` | Defines your immutable `Character` data class with name, class, description, and stat maps. |
| `CharacterCreatorUiState.kt` | Holds the `UiState` data class for your ViewModel to expose data to the UI. |
| `CharacterCreatorViewModel.kt` | Manages all app logic (updating fields, incrementing stats, and setting a selected character). |

You can also use the provided `DataSource.kt` for pre-made characters (e.g., “Warrior”, “Rogue”, “Mage”, etc.), which will populate your dropdown menu. You can modify it or create your own if you'd like.

---

## Tasks

### 🧩 1. Create a `CharacterCreatorViewModel`

- Create a class `CharacterCreatorViewModel` that extends `ViewModel`.


    ```kotlin
    import androidx.lifecycle.ViewModel

    class CharacterCreatorViewModel : ViewModel() {
    }
    ```

- You will first need to create a data class `CharacterCreatorUiState`, id recommend putting it in `CharacterCreatorUiState.kt`. In the beginning, the `CharacterCreatorUiState` can simply store a `Character`.

- After creating `CharacterCreatorUiState`, do the following:

  - Define a private `_uiState` that is a `MutableStateFlow` that holds a `CharacterCreatorUiState`.
  - Provide public access to the state with `val uiState: StateFlow<CharacterCreatorUiState> = _uiState.asStateFlow()` or similar.
  - Implement helper methods for updating the state:
    - `onNameChange(newName: String)`
    - `onClassChange(newClass: String)`
    - `onDescriptionChange(newDesc: String)`
    - `updateStat(statName: String, delta: Int)` and/or `updateStat(statIndxe: Int, delta: Int)`
    - `selectCharacter(character: Character)`

- Each of these will **copy** the existing state to produce a new one (immutability). The `updateStat` method can almost just use the `Character::updateStat()` method. You will then also need to **copy** the entire state with the updated `Character`

---

### 🧱 2. Move Logic from the Composables into the ViewModel

In Project 2, your composables handled all the `mutableStateOf` logic directly.  
Now, move that logic to the ViewModel.

- The ViewModel should track:
  - The current `Character`
  - Remaining points
  - Derived attributes (Attack, Defense, Cost)
- Your composables (`TextEntry`, `StatButtons`, and `AttributeList`) should only **display data** and call **ViewModel methods** for updates.

---


### ⚙️ 3. Connect the ViewModel to Your UI

In your `MainActivity`:

- Create a `ViewModel` object in your main level composable. For me, that is `CharacterCreatorApp`. I create a `viewModel` and `uiState` object like: 
   
    ```kotlin
    val viewModel: CharacterCreatorViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    ```

- Pass `uiState` and the ViewModel’s callback methods down to your composables.

    Example:
    ```kotlin
    TextEntry(
        character = uiState.character,
        onNameChange ={ /*TODO*/ }
        onClassChange = {/*TODO*/},
        onDescriptionChange = {/*TODO*/}
    )
    ```

    This way, your composables become **stateless** — they display whatever data the `ViewModel` provides.

---

### 🎯 4. Add a Dropdown for Character Selection

- Create a `DropdownMenu` or `ExposedDropdownMenuBox` and use it in place of the `TextField` that currently asks for the character's class.
- Load the character templates from `DataSource.defaultCharacters`.
- When a character is selected:
  - Call `viewModel.selectCharacter(character)`.
  - The text fields and stats should update automatically.

    
- This code should get you started with the DropdownMenu, or you can search online for other ways to have a DropdownMenu. One simple approach would be to have a Button that expands and collapses a DropdownMenu.

```kotlin
@Composable
fun ClassDropdownMenu(
    /* TODO you will need to pass things like which class is selected and a lambda for when
    *   the user chooses a character from the dropdown menu*/
    modifier: Modifier = Modifier
) {
    // expanded is just to store the state of the menu locally, the rest of the app doesn't
    // care if the menu is open or not
    var expanded by remember { mutableStateOf(false) }

    // defualtCharacters is a list of Characters with premade characters
    // This will also add "Custom" to the list. If you don't implement the "Custom"
    // character creation, you can either ignore it or just delete it for now
    val classOptions = defaultCharacters.map { it.charClass } + "Custom"

    Column(
        modifier = modifier
    ) {
        // This TextField can be used so the user can enter a custom class name
        // If you just want to keep with the default Characters, you can just use
        // it as a Text element. There are fields like `label`, `leadingIcon`, and
        // `readOnly` which you can use
        TextField(
            value = "", // what is actually displayed in the TextField
            onValueChange = { /* If you want the user to be able to enter a custom class
            name that would go here */
            },
            label = { /* This can simply be a Text composable that says "Class". You can get fancy
            and have a different label depending on if the menu is open or closed, or if the
            user needs to type their custom class name in the text field */
            },
            leadingIcon = { /* You can have this be the button that opens the menu  */
                IconButton(
                    onClick = {/* open/close the menu */ }
                ) {
                    // you can make this Icon be something different depending on if
                    // the menu is open or not.
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Select Class"
                    )
                }
            },
            readOnly = true, // control whether the user can type, true means they can't type
            modifier = Modifier.fillMaxWidth()
        )

        // The DropdownMenu is where you handle which class the user selects
        DropdownMenu(
            expanded = false,
            onDismissRequest = { },
        ) {
            // Here is where you display all the options and decide which one
            // is currentlySelected. Look at the ColorSelector DropdownMenu for reference
            // I created classOptions above which takes defaultCharacters and gets a list
            // of just the class Strings like: "Warrior", "Mage", "Rogue,...

        }
    }
}
```

When I add this composable in between the TextFields for the name and description, it looks like this:

![DropdownMenu from starter code](img/textEntryPreview.png)

---

### ⭐ Optional - “Custom” Character Option

Add one more option to your dropdown menu: **“Custom”**.  
When selected:
- Allow the user to enter their own character name and class in the text fields.  
- You may handle this by detecting when “Custom” is selected and not replacing the `Character` data from the `DataSource`.

---

### ⭐ Optional - Base Stat Mins 
Since the pre-made characters are given a certain set of stats, it would only make sense that the starting stats are the minimum for each stat. So, when decreasing a stat, you should ensure that doing so won't make it drop below the base stat for that character.

## Grading (25 pts)

| Task | Pts |
|------|-----:|
| ViewModel created and properly exposes `UiState` | 5 |
| Logic from composables moved into the ViewModel | 5 |
| Composables use ViewModel state and callbacks | 5 |
| Dropdown menu displays selectable characters | 5 |
| Selecting a character updates text and stats | 3 |
| App runs without crashes and layout remains organized | 2 |


---

## Submission

Take a **screenshot** showing your app running in the emulator (dropdown menu visible or character displayed).  
Zip your entire Android Studio project and the screenshot, then upload it to Dropbox.

---

## Next Steps

In **Project 4**, you’ll expand this app to include **multiple screens** — such as:
- a **Character List** screen where we will put the Name, Class, and Description input, as well as selecting an image for the CharacterCard,  
- a **Character Editor** screen where we will input the character stats, and  
- a **Final Card View** screen to display your completed character.
