package com.example.week09hw

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.week09hw.ui.theme.Week09hwTheme

@Composable
fun ActivityWrapper(root: @Composable ((PaddingValues) -> Unit)) {
    Week09hwTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            root(innerPadding)
        }
    }
}

fun rollDice(): Int {
    return (0..5).random()
}
