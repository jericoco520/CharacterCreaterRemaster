package com.example.charactercreator

import com.example.charactercreator.com.example.charactercreator.Character

/**
 * Represents the state of the Character Creator UI.
 *
 * This data class holds the current character being edited and derives additional UI-specific
 * properties from it, such as the remaining stat points and the calculated attributes. This
 * centralizes the logic for these derived values.
 *
 * @property character The current [Character] object being configured.
 */
data class CharacterCreatorUiState(
    val character: Character = Character(),
) {
    /**
     * The number of points left that can be allocated to stats.
     */
    val remainingPoints: Int = character.maxPoints - character.totalPoints

    /**
     * A map of derived attributes (e.g., "Attack", "Defense") calculated from the character's stats.
     */
    val attributes: Map<String, Int> = Character.computeAttributes(character.statMap)
}
