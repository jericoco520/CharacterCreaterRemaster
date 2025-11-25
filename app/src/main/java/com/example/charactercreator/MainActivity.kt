package com.example.charactercreator
import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charactercreator.com.example.charactercreator.Character
import com.example.charactercreator.com.example.charactercreator.rememberCharacterState
import com.example.charactercreator.com.example.charactercreator.statList
import com.example.charactercreator.com.example.charactercreator.updateStat
import com.example.charactercreator.ui.theme.CharacterCreatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharacterCreatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CharacterCreatorApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterCreatorApp(
    modifier: Modifier = Modifier,
) {
    // Use characterState to update the values in the character
    val characterState = rememberCharacterState()
    // Use character to access the values, but changing them here will not update the state!
    val character = characterState.value
    // User for calculating how many points are left
    val remainingPoints = character.maxPoints - character.totalPoints

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Character Creator", fontSize = 36.sp,
        )

        // Display inputtable character info
        CharacterInfoInput(
            character = character,
            onNameChange = { newName -> characterState.value = character.copy(name = newName) },
            onClassChange = { newClass -> characterState.value = character.copy(charClass = newClass) },
            onDescriptionChange = { newDescription ->
                characterState.value = character.copy(description = newDescription)
            }
        )

        // Display the allocatable stats
        StatGrid(characterState = characterState)

        // Display the remaining points
        PointsDisplay(pointsRemaining = remainingPoints, maxPoints = character.maxPoints)

        // Display summary of derived stats
        SummaryDisplay(character = character)
    }
}

/*
 * CharacterInfoInput Composable
 * Creates text fields for name, class, and description of the character. Users are allowed to input these values
 * which updates the character state.
 */
@Composable
fun CharacterInfoInput(
    character: Character,
    onNameChange: (String) -> Unit,
    onClassChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = character.name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = character.charClass,
            onValueChange = onClassChange,
            label = { Text("Class") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = character.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
    }
}

/*
 * StatGrid composable
 * Create a grid of StatControl composables.
 */
@Composable
fun StatGrid(characterState: MutableState<Character>, modifier: Modifier = Modifier) {
    val character = characterState.value
    val pointsUsed = character.totalPoints
    val maxPoints = character.maxPoints

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Going to iterate over the statList and create a StatControl for each stat
        items(statList) { statInfo ->
            // Get the current value of the stat from the character and default to zero if it doesn't exist
            val statValue = character.statMap[statInfo.name] ?: 0

            // Create a StatControl composable for the current stat
            StatControl(
                statName = statInfo.name,
                statValue = statValue,
                onIncrement = { updateStat(characterState, statInfo.name, 1, maxPoints) },
                onDecrement = { updateStat(characterState, statInfo.name, -1, maxPoints) },
                isIncrementEnabled = pointsUsed < maxPoints,
                isDecrementEnabled = statValue > 0
            )
        }
    }
}

/*
 * StatControl composable
 * Create a UI component for a stat that will allow the user to increment and decrement the stat.
 */
@Composable
fun StatControl(
    statName: String,
    statValue: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    isIncrementEnabled: Boolean,
    isDecrementEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Button that increments the stat's value
        Button(onClick = onIncrement, enabled = isIncrementEnabled) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_drop_up_24),
                contentDescription = "Increment"
            )
        }

        Text(text = statName)
        Text(text = statValue.toString())

        // Butotn that decrements the stat's value
        Button(onClick = onDecrement, enabled = isDecrementEnabled) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                contentDescription = "Decrement"
            )
        }
    }
}

/**
 * This composable will display the amount of available allocatable stat points for the character.
 * @param pointsRemaining will update as users increment stat values.
 */
@Composable
fun PointsDisplay(
    pointsRemaining: Int,
    maxPoints: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Points Remaining: $pointsRemaining/$maxPoints",
        fontSize = 15.sp,
        modifier = modifier
    )
}

/**
 * This composable will display the derived attributes such as attack, defense, and cost.
 *
 */
@Composable
fun SummaryDisplay(
    character: Character,
    modifier: Modifier = Modifier
) {
    // Get the derived stat values from the character
    val attack = character.attribMap.getOrDefault("Attack", 0)
    val defense = character.attribMap.getOrDefault("Defense", 0)
    val cost = character.attribMap.getOrDefault("Cost", 0)

    Column (
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "Attack: $attack")
        Text(text = "Defense: $defense")
        Text(text = "Cost: $cost")
    }
}

// TODO: AttributeList Composable
//  Needs: map of stats: values
// Optional: Modifier

@Preview(showBackground = true)
@Composable
fun CharacterCreatorPreview() {
    CharacterCreatorTheme {
        CharacterCreatorApp(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}
