package com.example.charactercreator

import androidx.lifecycle.ViewModel
import com.example.charactercreator.com.example.charactercreator.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterViewModel : ViewModel() {
    // Private as we want only ViewModel to update the UI state
    val _uiState = MutableStateFlow(CharacterCreatorUiState(character = Character()))

    // The public read-only UI state
    val uiState = _uiState.asStateFlow()
    //**************************************
    //** Event Handlers & State Updates **//
    //**************************************

    // Update the state's character with a new name
    fun onNameChange (newName: String) {
        _uiState.value = _uiState.value.copy(
            character = _uiState.value.character.copy(name = newName)
        )
    }

    // Update the state's character with a new class
    fun onClassChange (newClass: String) {
        _uiState.value = _uiState.value.copy(
            character = _uiState.value.character.copy(charClass = newClass)
        )
    }
}