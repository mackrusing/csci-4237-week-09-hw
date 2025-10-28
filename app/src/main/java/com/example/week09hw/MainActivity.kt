package com.example.week09hw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // call parent onCreate
        super.onCreate(savedInstanceState)

        // setup ui
        enableEdgeToEdge()
        setContent {
            ActivityWrapper { innerPadding ->
                ActivityRoot(innerPadding)
            }
        }
    }

}

private val dieArray = arrayOf(
    R.drawable.dice01,
    R.drawable.dice02,
    R.drawable.dice03,
    R.drawable.dice04,
    R.drawable.dice05,
    R.drawable.dice06
)

@Composable
private fun ActivityRoot(innerPadding: PaddingValues) {

    // state
    var p1Score by remember { mutableIntStateOf(0) }
    var p2Score by remember { mutableIntStateOf(0) }

    var p1DieIdx1 by remember { mutableIntStateOf(0) }
    var p1DieIdx2 by remember { mutableIntStateOf(0) }
    var p2DieIdx1 by remember { mutableIntStateOf(0) }
    var p2DieIdx2 by remember { mutableIntStateOf(0) }

    // handlers
    fun handleNewGame() {
        p1Score = 0
        p2Score = 0
        p1DieIdx1 = 0
        p1DieIdx2 = 0
        p2DieIdx1 = 0
        p2DieIdx2 = 0
    }

    fun handleP1Roll() {
        p1DieIdx1 = rollDice()
        p1DieIdx2 = rollDice()
        p1Score += p1DieIdx1 + p1DieIdx2 + 2
    }

    fun handleP2Roll() {
        p2DieIdx1 = rollDice()
        p2DieIdx2 = rollDice()
        p2Score += p2DieIdx1 + p2DieIdx2 + 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // player 1
        PlayerView(p1DieIdx1, p1DieIdx2, onClick = { handleP1Roll() })

        // scores
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "P1: $p1Score", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { handleNewGame() }, modifier = Modifier.padding(24.dp)) {
                Text(text = "New Game")
            }
            Text(text = "P2: $p2Score", style = MaterialTheme.typography.headlineMedium)
        }

        // player 2
        PlayerView(p2DieIdx1, p2DieIdx2, onClick = { handleP2Roll() })
    }

}

@Preview(showBackground = true)
@Composable
private fun ActivityRootPreview() {
    ActivityWrapper { innerPadding ->
        ActivityRoot(innerPadding)
    }
}

@Composable
private fun PlayerView(idx1: Int, idx2: Int, onClick: (() -> Unit)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(12.dp)
        ) {
            Die(idx1)
            Spacer(modifier = Modifier.width(12.dp))
            Die(idx2)
        }
    }
}

@Composable
private fun Die(idx: Int) {
    
    Image(
        painter = painterResource(id = dieArray[idx]),
        contentDescription = null,
        modifier = Modifier.width(128.dp)
    )
}
