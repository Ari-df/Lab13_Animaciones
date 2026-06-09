package com.example.lab13animaciones.potion

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Cauldron(
    liquidColor: Color,
    isExploding: Boolean,
    selectedCount: Int
) {
    val animatedColor by animateColorAsState(
        targetValue = liquidColor,
        animationSpec = tween(800),
        label = "liquidColor"
    )

    // Rotación de cuchara
    val infiniteTransition = rememberInfiniteTransition(label = "spoon")
    val spoonRotation by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "spoonRotation"
    )

    // Ondas del líquido
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Cuchara
        Box(
            modifier = Modifier
                .rotate(spoonRotation)
                .padding(bottom = 4.dp)
        ) {
            Text("🥄", fontSize = 28.sp)
        }

        // Caldero
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 130.dp)
                .border(
                    width = 4.dp,
                    color = Color(0xFF3D2B1F),
                    shape = RoundedCornerShape(
                        topStart = 8.dp, topEnd = 8.dp,
                        bottomStart = 50.dp, bottomEnd = 50.dp
                    )
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 8.dp, topEnd = 8.dp,
                        bottomStart = 50.dp, bottomEnd = 50.dp
                    )
                )
                .background(Color(0xFF1A1A1A))
        ) {
            // Líquido animado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                animatedColor.copy(alpha = 0.6f),
                                animatedColor,
                                animatedColor.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            // Onda superficial
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .offset(y = waveOffset.dp)
                    .align(Alignment.Center)
                    .background(
                        animatedColor.copy(alpha = 0.4f),
                        RoundedCornerShape(50)
                    )
            )

            // Burbujas dentro del caldero
            if (selectedCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    BubbleEffect(color = animatedColor)
                }
            }

            // Explosión
            if (isExploding) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ExplosionEffect()
                }
            }
        }

        // Patas del caldero
        Row(
            modifier = Modifier.width(140.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(18.dp)
                        .background(
                            Color(0xFF3D2B1F),
                            RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                        )
                )
            }
        }

        // Fuego debajo
        Text(
            text = "🔥 🔥 🔥",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}