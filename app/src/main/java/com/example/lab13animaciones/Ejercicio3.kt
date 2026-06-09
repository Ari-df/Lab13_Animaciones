package com.example.lab13animaciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio3() {
    var expanded by remember { mutableStateOf(false) }

    val size by animateDpAsState(
        targetValue = if (expanded) 200.dp else 100.dp,
        animationSpec = tween(durationMillis = 800),
        label = "sizeAnimation"
    )

    val offsetY by animateDpAsState(
        targetValue = if (expanded) 50.dp else 0.dp,
        animationSpec = tween(durationMillis = 800),
        label = "offsetAnimation"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .offset(y = offsetY)
                .size(size)
                .background(Color.Red)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Reducir" else "Expandir")
        }
    }
}