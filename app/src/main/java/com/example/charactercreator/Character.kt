/**
 * Character.kt
 *
 * This file contains the Character class definition, as well some other data such as
 * what Icon is associated with each stat/attribute, stored in statList and attribList.
 */
package com.example.charactercreator

import androidx.annotation.DrawableRes

// this little class is just to group the stat/attribute names with their icon and a short
// description
data class StatInfo(
    val name: String, @DrawableRes val icon: Int, val description: String
)

// statList has a StatInfo for each stat to group the name, icon, and short description of each stst
val statList = listOf(
    StatInfo("Power", R.drawable.construction_24px, "Increases attack damage"),
    StatInfo("Endurance", R.drawable.baseline_fitness_center_24, "Improves defense"),
    StatInfo("Speed", R.drawable.baseline_bolt_24, "Boosts agility"),
    StatInfo("Focus", R.drawable.baseline_visibility_24, "Reduces cost")
)

// similar to statList, attrib list has a StatInfo for each attribute
val attribList = listOf(
    StatInfo("Attack", R.drawable.swords_24px, "Amount of damage done"),
    StatInfo("Defense", R.drawable.baseline_security_24, "Amount of damage blocked"),
    StatInfo("Cost", R.drawable.baseline_price_change_24, "Cost for casting this character"),
)

/**
 * All the fields of Character have a default initial value, so you only need to give it the
 * fields you want.
 *
 * @property name String of the character name
 * @property charClass String of the character's class
 * @property description String of the character description
 * @property statMap map of (statName: statValue)
 * @property maxPoints The max sum of points that can be spend on character stats
 */
data class Character(
    val name: String = "",
    val charClass: String = "",
    val description: String = "",
    val statMap: Map<String, Int> = mapOf(
        "Power" to 0, "Endurance" to 0, "Speed" to 0, "Focus" to 0
    ),
    val maxPoints: Int = 10
) {

    /**
     * Total points spent on stats.
     */
    val totalPoints: Int
        get() = statMap.values.sum()

    /**
     * This version of withUpdatedStat adds delta to the stat with the key
     * statName, and returns a new character with the updated statMap. The
     * stat won't be updated if the sum of stats has reached maxPoints, or if
     * doing so would make it go below 0
     *
     * @param statName String for the stat to update in statMap
     * @param delta how much to change the stat by (typically +/- 1)
     * @return a copy of the character with an updated statMap
     */
    fun withUpdatedStat(statName: String, delta: Int): Character {
        val newStats = statMap.toMutableMap()
        val currentValue = newStats[statName] ?: 0
        val newValue = (currentValue + delta).coerceAtLeast(0)

        // Prevent exceeding total pool
        if (delta > 0 && totalPoints >= maxPoints) return this
        if (newValue == currentValue) return this

        newStats[statName] = newValue
        return copy(statMap = newStats)
    }

    companion object {
        /**
         * given the Map, compute the attack, defense, and cost of the character
         *
         * @param stats a map of (stat: value)
         * @return a map of (attribute: value)
         */
        fun computeAttributes(stats: Map<String, Int>): Map<String, Int> {
            val power = stats["Power"] ?: 0
            val endurance = stats["Endurance"] ?: 0
            val speed = stats["Speed"] ?: 0
            val focus = stats["Focus"] ?: 0

            val attack = (2 * power) + (speed / 2)
            val defense = (2 * endurance) + (speed / 2)
            val cost = ((attack + defense) / 4) + (focus / 2)

            return mapOf(
                "Attack" to attack, "Defense" to defense, "Cost" to cost
            )
        }
    }
}

// Helper function to make a Character more easily
fun makeCharacter(
    name: String = "",
    charClass: String = "",
    description: String = "",
    power: Int = 0,
    endurance: Int = 0,
    speed: Int = 0,
    focus: Int = 0
): Character {
    val statsMap = mapOf(
        "Power" to power,
        "Endurance" to endurance,
        "Speed" to speed,
        "Focus" to focus
    )

    return Character(
        name = name,
        charClass = charClass,
        description = description,
        statMap = statsMap,
    )
}
