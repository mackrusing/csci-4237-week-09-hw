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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.media.MediaPlayer
import android.provider.MediaStore
import android.widget.Toast


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


    //adding turns
    var p1Turns by remember { mutableIntStateOf(0) }
    var p2Turns by remember { mutableIntStateOf(0) }


    //messages
    var specialMessage by remember { mutableStateOf("") }
    var winnerMessage by remember { mutableStateOf("") }


    // handlers
    fun handleNewGame() {
        p1Score = 0
        p2Score = 0
        p1DieIdx1 = 0
        p1DieIdx2 = 0
        p2DieIdx1 = 0
        p2DieIdx2 = 0

        //specific turns
        p1Turns = 0
        p2Turns = 0
        specialMessage = ""
        winnerMessage = ""
    }


    fun checkGameOver() {
        if (p1Turns >= 10 && p2Turns >= 10) {
            winnerMessage = when {
                p1Score > p2Score -> "Player 1 Wins"
                p2Score > p1Score -> "Player 2 Wins"
                else -> "It's a tie"
            }
        }
    }



    //hissing stuff
    val context = androidx.compose.ui.platform.LocalContext.current

    fun playHissSound() {
        val mp = MediaPlayer.create(context, R.raw.snake_hiss)
        mp.start()
        mp.setOnCompletionListener { it.release() }
    }

    fun playDesertSound(){
        val mp = MediaPlayer.create(context, R.raw.desert)
        mp.start()
        mp.setOnCompletionListener { it.release() }
    }

    fun handleP1Roll() {
        if (p1Turns >= 10) return

        p1DieIdx1 = rollDice()
        p1DieIdx2 = rollDice()

        //make logic easier to deal w, don't hve to start w idx 0
        val roll1 = p1DieIdx1 + 1
        val roll2 = p1DieIdx2 + 1
        var turnScore = roll1 + roll2
        //two sixes
        var bonusTurn = false

        //reset
        specialMessage = ""

        //special rolls
        when {
            roll1 == 1 && roll2 == 1 -> {
                specialMessage = "Snake Eyes! +10 points!"
                turnScore += 10

                //toast, snake
                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.snake)
                val toast = Toast(context)
                toast.view = imageView
                toast.duration = Toast.LENGTH_SHORT
                toast.show()
                //hissing sound
                playHissSound()
            }
            roll1 == 2 && roll2 == 2 -> {
                specialMessage = "two+two is 4. 4 extra points."
                turnScore += 4
            }
            roll1 == 6 && roll2 == 6 -> {
                specialMessage = "two sixes, extra turn."
                bonusTurn = true
            }
            roll1 == 3 && roll2 == 3 -> {
                specialMessage = "Camels, you lose."
                turnScore -= 10000

                //toast, snake
                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.camel)
                val toast = Toast(context)
                toast.view = imageView
                playDesertSound()
                toast.duration = Toast.LENGTH_SHORT
                toast.show()

            }
        }

        p1Score += turnScore
        if (!bonusTurn) p1Turns++
        checkGameOver()
    }

    fun handleP2Roll() {
        if (p2Turns >= 10) return

        p2DieIdx1 = rollDice()
        p2DieIdx2 = rollDice()
        //make logic easier to deal w
        val roll1 = p2DieIdx1 + 1
        val roll2 = p2DieIdx2 + 1
        var turnScore = roll1 + roll2
        //two sixes
        var bonusTurn = false

        //reset
        specialMessage = ""

        //special rolls
        when {
            roll1 == 1 && roll2 == 1 -> {
                specialMessage = "Snake Eyes! +10 points!"
                turnScore += 10

                //toast, snake
                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.snake)
                val toast = Toast(context)
                toast.view = imageView
                toast.duration = Toast.LENGTH_SHORT
                toast.show()
                //hissing sound
                playHissSound()
            }
            roll1 == 2 && roll2 == 2 -> {
                specialMessage = "two+two is 4. 4 extra points."
                turnScore += 4
            }
            roll1 == 6 && roll2 == 6 -> {
                specialMessage = "two sixes, extra turn."
                bonusTurn = true
            }
            roll1 == 3 && roll2 == 3 -> {
                specialMessage = "Camels, you lose."
                turnScore -= 1000

                //toast, camel
                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.camel)
                val toast = Toast(context)
                toast.view = imageView
                toast.duration = Toast.LENGTH_LONG
                toast.show()
                playDesertSound()
            }
        }

        p2Score += turnScore
        if (!bonusTurn) p2Turns++
        checkGameOver()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        //p1
        PlayerView(
            idx1 = p1DieIdx1,
            idx2 = p1DieIdx2,
            onClick = { handleP1Roll() },
            label = "Player 1",
            turns = p1Turns
        )

        //score+buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "P1: $p1Score",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "P2: $p2Score",
                style = MaterialTheme.typography.headlineMedium
            )

            if (specialMessage.isNotEmpty()) {
                Text(
                    text = specialMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(12.dp)
                )
            }

            if (winnerMessage.isNotEmpty()) {
                Text(
                    text = winnerMessage,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Button(
                onClick = { handleNewGame() },
                modifier = Modifier.padding(24.dp)
            ) {
                Text("New Game")
            }
        }

        //plyr 2
        PlayerView(
            idx1 = p2DieIdx1,
            idx2 = p2DieIdx2,
            onClick = { handleP2Roll() },
            label = "Player 2",
            turns = p2Turns
        )
    }
}
@Composable
private fun PlayerView(idx1: Int, idx2: Int, onClick: () -> Unit, label: String, turns: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$label — Turn ${turns + 1}/11")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
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

@Preview(showBackground = true)
@Composable
private fun ActivityRootPreview() {
    ActivityWrapper { innerPadding ->
        ActivityRoot(innerPadding)
    }
}
