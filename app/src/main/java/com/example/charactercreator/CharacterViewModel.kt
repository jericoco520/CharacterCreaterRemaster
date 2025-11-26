package com.example.charactercreator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CharacterViewModel : ViewModel() {
    // Private mutable state
    private val _uiState = MutableStateFlow(CharacterCreatorUiState())

    // Public read-only state
    val uiState: StateFlow<CharacterCreatorUiState> = _uiState.asStateFlow()

    /**
     * Updates the character's name in the UI state.
     */
    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(character = it.character.copy(name = newName))
        }
    }

    /**
     * Updates the character's class in the UI state.
     */
    fun onClassChange(newClass: String) {
        _uiState.update {
            it.copy(character = it.character.copy(charClass = newClass))
        }
    }

    /**
     * Updates the character's description in the UI state.
     */
    fun onDescriptionChange(newDescription: String) {
        _uiState.update {
            it.copy(character = it.character.copy(description = newDescription))
        }
    }

    /**
     * Updates a specific stat for the character by a given delta.
     *
     * This function will not allow the total stat points to exceed the character's maxPoints
     * or for a stat to go below zero.
     *
     * @param statName The name of the stat to update (e.g., "Power").
     * @param delta The amount to change the stat by (typically +1 or -1).
     */
    fun updateStat(statName: String, delta: Int) {
        _uiState.update {
            val updatedCharacter = it.character.withUpdatedStat(statName, delta)
            it.copy(character = updatedCharacter)
        }
    }
}
