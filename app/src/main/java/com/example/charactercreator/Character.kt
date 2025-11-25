/**
 * Character.kt
 *
 * This file contains the Character class definition, as well some other data such as
 * what Icon is associated with each stat/attribute, stored in statList and attribList.
 *
 * To create a persistent Character object, you can use rememberCharacterState() which returns a
 * mutable state Character object that is remembered after function calls
 *
 * To update character fields such as name, charClass, or description, you would use the characterState
 * you get from rememberCharacterState() and reassign it's `value` with a copy of the current state
 * Which if updating the name, looks like:
 *
 * characterState.value = characterState.value.copy(name = newName)
 *
 * That will create a copy of the current value and replace the name, and reassign the state
 * to this new copy.
 *
 * There are two ways to store Character stats/attributes. One uses a map of (name: value) and the
 * other uses arrays that you would use in parallel with the `statList` and `attribList` provided
 *
 * To update a stat, you would call
 * updateStat(characterState, statString or statIndex, +/- 1, character.maxPoints)
 *
 * This will reassign characterState.value to the updated Character. The attributes are updated
 * during this function call, so the resulting state has the correct everything
 *
 * NOTE: You cannot use both statMap and statArray, you can only use one or the other to store the
 * character data. If you want to, you can comment out the one you aren't using just to make sure.
 * If so, also need to comment out all methods/functions that take either a Map or an Array.
 * Basically anything that is red after you comment out either statMap or statArray
 */
package com.example.charactercreator.com.example.charactercreator

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.charactercreator.R

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
 * fields you want. I recommend NOT giving it a statMap/statArray nor a attribMap/attribArray. Those
 * are both initialized to work with the stats and attributes listed in the statList and attribList.
 * You would probably actually want all the properties to be private but we are just making them
 * public so it is easier to use.
 *
 * @property name String of the character name
 * @property charClass String of the character's class
 * @property description String of the character description
 * @property statMap map of (statName: statValue)
 * @property statArray array of stat values in parallel with statList
 * @property attribMap map of (attribName: attribValue)
 * @property attribArray array of attribute values in parallel with attribList
 * @property maxPoints The max sum of points that can be spend on character stats
 */
data class Character(
    // *********************************************************************************************
    // Character properties
    // *********************************************************************************************

    //**********************************
    // Character strings
    // We could put these in a map or array (if you want you can try one of those)
    // but just having them as individual variables is okay
    val name: String = "", val charClass: String = "", val description: String = "",


    // ************************************
    // Character stats
    // There are two approaches to storing the character stats
    // this one involves using a Map of (statName: statValue)
    val statMap: Map<String, Int> = mapOf(
        "Power" to 0, "Endurance" to 0, "Speed" to 0, "Focus" to 0
    ),

    // Another option is to use an array where index 0 in statArray represents the same
    // stat as index 0 does in statsList above (Power), and index 1 is the stat at index 1
    // in statList (Endurance), etc.
    val statArray: Array<Int> = arrayOf(0, 0, 0, 0),

    // can use individual variables
    // I don't recommend this as it will be more work
    // val power: Int = 0,
    // val endurance: Int = 0,

    // *******************************************
    // Character attributes
    // There are also two approaches to storing the attributes
    // attribMap maps (attribName: attribValue)
    val attribMap: Map<String, Int> = mapOf(
        "Attack" to 0, "Defense" to 0, "Cost" to 0
    ),

    // Similar to statArray, the value at index 0 of attribArray represents the value
    // of the attribute located at index 0 in attribList (Attack), index 1 is the attribute
    // at index 1 in attribList (Defensee), etc.
    val attribArray: Array<Int> = arrayOf(0, 0, 0),

    // You can modify this to give more flexibility, but it should remain constant once a character
    // is created. Although, you could think of a situation where a specific class gets a different
    // number of maxPoints...
    val maxPoints: Int = 10
) {

    // *********************************************************************************************
    // Character methods
    // *********************************************************************************************

    /**
     * Total points
     * Because of how I'm calculating total points here, you should only either
     * use the array or map to store the stats.
     */
    val totalPoints: Int
        get() = (statMap.values.sum() + statArray.sum())

    /**
     * This version of withUpdated stat added delta to the stat with the key
     * statName, and returns a new character with the updated statMap. The
     * stat won't be updated if the sum of stats has reached maxPoints, or if
     * doing so would make it go below 0
     *
     * @param statName String for the stat to update in statMap
     * @param delta how much to change the stat by (typically +/- 1)
     * @param maxPoints value the sum of points cannot exceed
     * @return a copy of the character with an updated statMap
     */
    fun withUpdatedStat(statName: String, delta: Int, maxPoints: Int): Character {
        val newStats = statMap.toMutableMap()
        val currentValue = newStats[statName] ?: 0
        val newValue = (currentValue + delta).coerceAtLeast(0)

        // Prevent exceeding total pool
        if (delta > 0 && totalPoints >= maxPoints) return this
        if (newValue == currentValue) return this

        newStats[statName] = newValue
        return copy(
            statMap = newStats, attribMap = computeAttributes(newStats)
        )
    }

    /**
     * This version of withUpdated stat added delta to the stat at index statIndex in
     * statArray, and returns a new character with the updated statArray. The
     * stat won't be updated if the sum of stats has reached maxPoints, or if
     * doing so would make it go below 0
     *
     * @param statindex index for the stat to update in statArray
     * @param delta how much to change the stat by (typically +/- 1)
     * @param maxPoints value the sum of points cannot exceed
     * @return a copy of the character with an updated statArray
     */
    fun withUpdatedStat(statIndex: Int, delta: Int, maxPoints: Int): Character {
        val newStats = statArray.clone()
        val currentValue = newStats[statIndex]
        val newValue = (currentValue + delta).coerceAtLeast(0)

        // Prevent exceeding total pool
        if (delta > 0 && totalPoints >= maxPoints) return this
        if (newValue == currentValue) return this

        newStats[statIndex] = newValue
        return copy(
            statArray = newStats, attribArray = computeAttributes(newStats)
        )
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

            val attack = (2 * stats.getOrDefault("Power", 0)) + (stats.getOrDefault("Speed", 0) / 2)
            val defense =
                (2 * stats.getOrDefault("Endurance", 0)) + (stats.getOrDefault("Speed", 0) / 2)
            val cost = ((attack + defense) / 4) + (stats.getOrDefault("Focus", 0) / 2)

            return mapOf(
                "Attack" to attack, "Defense" to defense, "Cost" to cost
            )
        }

        /**
         * given the array, calculate the attack, defense, and cost of the character
         *
         * @param stats array of character stats
         * @return array with character attributes
         */
        fun computeAttributes(stats: Array<Int>): Array<Int> {
            val power = stats[0]
            val endurance = stats[1]
            val speed = stats[2]
            val focus = stats[3]

            val attack = (2 * power) + (speed / 2)
            val defense = (2 * endurance) + (speed / 2)
            val cost = ((attack + defense) / 4) + (focus / 2)

            return arrayOf(
                attack, defense, cost
            )
        }
    }

    init {
        println("Character recomposed: name=$name")
    }
}

//**************************************************************************************************
// Helper functions
// *************************************************************************************************

/**
 * This lets us simply call rememberCharacterState() instead of using
 * remember {mutableStateOf()}
 *
 * @return MutableState for a character that will persist after a function call ends
 */
@Composable
fun rememberCharacterState(): MutableState<Character> {
    return remember { mutableStateOf(Character()) }
}

/**
 * Helper function to update the given stat based on a String. The characterState.value is assigned
 * to a different Character if a change has occured, which will trigger recomposition
 *
 * @param characterState mutable state, the value field is reassigned which will trigger recomposition
 * @param statName String of the stat in statMap to be updated
 * @param delta how much (usually +/- 1) to change the stat by
 * @param maxPoints the max limit for the sum of points, so we don't go over
 */
fun updateStat(
    characterState: MutableState<Character>, statName: String, delta: Int, maxPoints: Int
) {
    val current = characterState.value
    val updated = current.withUpdatedStat(statName, delta, maxPoints)
    if (updated != current) {
        characterState.value = updated
    }
}

/**
 * Helper function to update the given stat based on an index. The characterState.value is assigned
 * to a different Character if a change has occured, which will trigger recomposition
 *
 * @param characterState mutable state, the value field is reassigned which will trigger recomposition
 * @param statIndex Index of the stat in statArray to be updated
 * @param delta how much (usually +/- 1) to change the stat by
 * @param maxPoints the max limit for the sum of points, so we don't go over
 */
fun updateStat(
    characterState: MutableState<Character>, statIndex: Int, delta: Int, maxPoints: Int
) {
    val current = characterState.value
    val updated = current.withUpdatedStat(statIndex, delta, maxPoints)
    if (updated != current) {
        characterState.value = updated
    }
}
