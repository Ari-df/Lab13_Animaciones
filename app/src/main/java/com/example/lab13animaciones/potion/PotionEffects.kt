package com.example.lab13animaciones.potion

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun BubbleEffect(color: Color) {
    val bubbles = remember { List(6) { Random.nextFloat() } }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        bubbles.forEachIndexed { index, offset ->
            val infiniteTransition = rememberInfiniteTransition(label = "bubble_$index")

            val yOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -60f - offset * 40f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 800 + (index * 150),
                        easing = EaseInOut
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "bubbleY_$index"
            )

            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.9f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800 + (index * 150)),
                    repeatMode = RepeatMode.Restart
                ),
                label = "bubbleAlpha_$index"
            )

            val size = (6 + index * 2).dp

            Box(
                modifier = Modifier
                    .offset(
                        x = (offset * 20 - 10).dp,
                        y = yOffset.dp
                    )
                    .size(size)
                    .alpha(alpha)
                    .background(
                        color.copy(alpha = 0.7f),
                        CircleShape
                    )
            )
        }
    }
}

@Composable
fun SparkleEffect() {
    val sparkles = remember { List(8) { Pair(Random.nextFloat(), Random.nextFloat()) } }

    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkleScale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkleAlpha"
    )

    Box(modifier = Modifier.size(200.dp)) {
        sparkles.forEach { (x, y) ->
            Box(
                modifier = Modifier
                    .offset(
                        x = (x * 180).dp,
                        y = (y * 180).dp
                    )
                    .scale(scale)
                    .alpha(alpha)
                    .size(6.dp)
                    .background(Color(0xFFFFD700), CircleShape)
            )
        }
    }
}

@Composable
fun ExplosionEffect() {
    val particles = remember { List(10) { Pair(Random.nextFloat() * 2 - 1f, Random.nextFloat() * 2 - 1f) } }

    val infiniteTransition = rememberInfiniteTransition(label = "explosion")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ),
        label = "explosionProgress"
    )

    Box(modifier = Modifier.size(120.dp)) {
        particles.forEach { (dx, dy) ->
            Box(
                modifier = Modifier
                    .offset(
                        x = (60 + dx * 50 * progress).dp,
                        y = (60 + dy * 50 * progress).dp
                    )
                    .alpha(1f - progress)
                    .size(8.dp)
                    .background(
                        if (progress < 0.5f) Color(0xFFFF4500) else Color(0xFFFF8C00),
                        CircleShape
                    )
            )
        }
    }
}