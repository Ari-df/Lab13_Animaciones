package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class EstadoPantalla { CARGANDO, CONTENIDO, ERROR }

@Composable
fun Ejercicio4() {
    var estado by remember { mutableStateOf(EstadoPantalla.CARGANDO) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                fadeIn(animationSpec = tween(600)) togetherWith
                        fadeOut(animationSpec = tween(600))
            },
            label = "estadoAnimation"
        ) { targetEstado ->
            when (targetEstado) {
                EstadoPantalla.CARGANDO -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando...", fontSize = 18.sp)
                    }
                }
                EstadoPantalla.CONTENIDO -> {
                    Text(
                        "¡Contenido cargado exitosamente!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
                EstadoPantalla.ERROR -> {
                    Text(
                        "❌ Ocurrió un error",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { estado = EstadoPantalla.CARGANDO }) { Text("Cargando") }
            Button(onClick = { estado = EstadoPantalla.CONTENIDO }) { Text("Contenido") }
            Button(onClick = { estado = EstadoPantalla.ERROR }) { Text("Error") }
        }
    }
}