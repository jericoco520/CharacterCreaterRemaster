package com.example.charactercreator

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.charactercreator.ui.theme.CharacterCreatorTheme
import com.example.charactercreator.data.DataSource

// A reusable composable for character selection
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterSelectionDropdown(
    characters: List<Character>,
    selectedCharacter: Character,
    onCharacterSelected: (Character) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        // This is the TextField that shows the current selection
        TextField(
            value = selectedCharacter.charClass,
            onValueChange = {}, // The text field is not directly editable
            readOnly = true,
            label = { Text("Select Class") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true) // This is important
        )

        // This is the actual dropdown menu
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            characters.forEach { character ->
                DropdownMenuItem(
                    text = { Text(character.charClass) },
                    onClick = {
                        onCharacterSelected(character)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropdownPreview() {
    CharacterSelectionDropdown(
        characters = DataSource.defaultCharacters,
        selectedCharacter = DataSource.defaultCharacters[0],
        onCharacterSelected = {}
    )
}