package com.example.lab13animaciones.potion

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IngredientCard(
    ingredient: Ingredient,
    isSelected: Boolean,
    isDisabled: Boolean,
    onClick: () -> Unit
) {
    var bounce by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (bounce) 1.2f else if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        finishedListener = { bounce = false },
        label = "ingredientScale"
    )

    val bgColor = if (isSelected)
        ingredient.color.copy(alpha = 0.3f)
    else
        Color(0xFF2A1F3D)

    val borderColor = if (isSelected) ingredient.color else Color(0xFF4A3F6B)

    Box(
        modifier = Modifier
            .scale(scale)
            .size(width = 80.dp, height = 90.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(bgColor, RoundedCornerShape(12.dp))
            .clickable(enabled = !isDisabled) {
                bounce = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = ingredient.emoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ingredient.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) ingredient.color else Color(0xFFB0A8C8)
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 10.sp,
                    color = ingredient.color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}