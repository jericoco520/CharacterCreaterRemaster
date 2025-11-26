package com.example.charactercreator.data
import com.example.charactercreator.makeCharacter


object DataSource {
    // Create a list of four default characters
    // It's our responsibility to make sure the points don't go past the max point total
    // OR - we can use these points to figure out what the point total should be for any character
    val defaultCharacters = listOf(
        makeCharacter(
            name = "Thorin",
            charClass = "Warrior",
            description = "A battle-hardened fighter who relies on brute strength and endurance.",
            power = 4,
            endurance = 3,
            speed = 2,
            focus = 1
        ),
        makeCharacter(
            name = "Lyra",
            charClass = "Rogue",
            description = "A quick and cunning thief who strikes from the shadows.",
            power = 2,
            endurance = 2,
            speed = 4,
            focus = 2
        ),
        makeCharacter(
            name = "Seraphine",
            charClass = "Paladin",
            description = "A holy knight devoted to protecting allies and vanquishing evil.",
            power = 3,
            endurance = 3,
            speed = 2,
            focus = 2
        ),
        makeCharacter(
            name = "Eldrin",
            charClass = "Mage",
            description = "A master of arcane arts who channels focus into devastating spells.",
            power = 1,
            endurance = 1,
            speed = 1,
            focus = 7
        )
    )
}