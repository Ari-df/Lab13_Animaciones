package com.example.lab13animaciones.potion

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PotionGameScreen() {
    var selectedIngredients by remember { mutableStateOf<List<Int>>(emptyList()) }
    var currentPotion by remember { mutableStateOf<Potion?>(null) }
    var isExploding by remember { mutableStateOf(false) }
    var gameState by remember { mutableStateOf<GameState>(GameState.Brewing) }

    // Color del líquido según ingredientes seleccionados
    val liquidColor = when {
        selectedIngredients.isEmpty() -> Color(0xFF1A0A2E)
        selectedIngredients.size == 1 -> INGREDIENTS[selectedIngredients[0]].color.copy(alpha = 0.7f)
        selectedIngredients.size == 2 -> blendColors(
            INGREDIENTS[selectedIngredients[0]].color,
            INGREDIENTS[selectedIngredients[1]].color
        )
        else -> getPotion(selectedIngredients)?.color ?: Color(0xFF8B0000)
    }

    // Fondo animado
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val bgAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0620),
                        Color(0xFF1A0A2E),
                        Color(0xFF0D0620)
                    )
                )
            )
    ) {
        // Estrellas de fondo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(bgAlpha)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            liquidColor.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text = "⚗️ POTION CRAFT",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE8D5FF),
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 40.dp)
            )
            Text(
                text = "Mezcla 3 ingredientes",
                fontSize = 13.sp,
                color = Color(0xFF9B89C4),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Caldero
            Box(contentAlignment = Alignment.Center) {
                Cauldron(
                    liquidColor = liquidColor,
                    isExploding = isExploding,
                    selectedCount = selectedIngredients.size
                )
                // Destellos si hay poción rara
                if (currentPotion?.isRare == true && gameState is GameState.Result) {
                    SparkleEffect()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Panel de resultado
            AnimatedContent(
                targetState = gameState,
                transitionSpec = {
                    fadeIn(tween(400)) + scaleIn(tween(400)) togetherWith
                            fadeOut(tween(200))
                },
                label = "gameState"
            ) { state ->
                when (state) {
                    is GameState.Brewing -> {
                        // Ingredientes seleccionados
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(3) { slot ->
                                val ingredientId = selectedIngredients.getOrNull(slot)
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            if (ingredientId != null)
                                                INGREDIENTS[ingredientId].color.copy(alpha = 0.2f)
                                            else
                                                Color(0xFF2A1F3D),
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (ingredientId != null)
                                            INGREDIENTS[ingredientId].emoji
                                        else "?",
                                        fontSize = if (ingredientId != null) 22.sp else 18.sp,
                                        color = Color(0xFF6B5F8A)
                                    )
                                }
                                if (slot < 2) {
                                    Text(
                                        " + ",
                                        color = Color(0xFF6B5F8A),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    is GameState.Result -> {
                        val potion = state.potion
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${potion.emoji} ${potion.name}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = potion.color
                            )
                            Text(
                                text = potion.description,
                                fontSize = 13.sp,
                                color = Color(0xFFB0A8C8),
                                textAlign = TextAlign.Center
                            )
                            if (potion.isRare) {
                                Text(
                                    text = "✨ ¡POCIÓN RARA! ✨",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    is GameState.Explosion -> {
                        Text(
                            text = "💥 ¡MEZCLA INCORRECTA!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF4500),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Grid de ingredientes
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                INGREDIENTS.chunked(3).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { ingredient ->
                            val isSelected = ingredient.id in selectedIngredients
                            val isDisabled = selectedIngredients.size >= 3 && !isSelected ||
                                    gameState is GameState.Result ||
                                    gameState is GameState.Explosion

                            IngredientCard(
                                ingredient = ingredient,
                                isSelected = isSelected,
                                isDisabled = isDisabled,
                                onClick = {
                                    if (isSelected) {
                                        selectedIngredients = selectedIngredients - ingredient.id
                                    } else if (selectedIngredients.size < 3) {
                                        selectedIngredients = selectedIngredients + ingredient.id
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AnimatedVisibility(
                    visible = selectedIngredients.size == 3 && gameState is GameState.Brewing,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Button(
                        onClick = {
                            val potion = getPotion(selectedIngredients)
                            if (potion != null) {
                                currentPotion = potion
                                gameState = GameState.Result(potion)
                            } else {
                                isExploding = true
                                gameState = GameState.Explosion
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B35A8)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("⚗️ ¡Mezclar!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                AnimatedVisibility(
                    visible = gameState !is GameState.Brewing || selectedIngredients.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = {
                            selectedIngredients = emptyList()
                            currentPotion = null
                            isExploding = false
                            gameState = GameState.Brewing
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3D2B5A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("🔄 Limpiar", color = Color(0xFFB0A8C8))
                    }
                }
            }
        }
    }
}

// Estados del juego
sealed class GameState {
    object Brewing : GameState()
    data class Result(val potion: Potion) : GameState()
    object Explosion : GameState()
}

// Mezclar dos colores
fun blendColors(c1: Color, c2: Color): Color = Color(
    red = (c1.red + c2.red) / 2f,
    green = (c1.green + c2.green) / 2f,
    blue = (c1.blue + c2.blue) / 2f,
    alpha = 1f
)