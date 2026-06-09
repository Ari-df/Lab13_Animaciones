package com.example.lab13animaciones.potion

import androidx.compose.ui.graphics.Color

data class Ingredient(
    val id: Int,
    val name: String,
    val emoji: String,
    val color: Color
)

data class Potion(
    val name: String,
    val emoji: String,
    val color: Color,
    val description: String,
    val isRare: Boolean = false
)

val INGREDIENTS = listOf(
    Ingredient(0, "Fuego",     "🔥", Color(0xFFFF4500)),
    Ingredient(1, "Hielo",     "❄️", Color(0xFF00BFFF)),
    Ingredient(2, "Veneno",    "☠️", Color(0xFF32CD32)),
    Ingredient(3, "Sombra",    "🌑", Color(0xFF4B0082)),
    Ingredient(4, "Luz",       "✨", Color(0xFFFFD700)),
    Ingredient(5, "Sangre",    "🩸", Color(0xFF8B0000))
)

// Recetas: combinación de 3 IDs de ingredientes → poción
val RECIPES: Map<Set<Int>, Potion> = mapOf(
    setOf(0, 1, 4) to Potion("Poción Aurora",    "🌈", Color(0xFFFF69B4), "¡Colores infinitos!", isRare = true),
    setOf(0, 2, 3) to Potion("Veneno Ardiente",  "🐍", Color(0xFF228B22), "Quema por dentro"),
    setOf(1, 4, 5) to Potion("Escarcha Mística", "💎", Color(0xFF87CEEB), "Congela el alma"),
    setOf(0, 3, 5) to Potion("Sangre Sombría",   "🌑", Color(0xFF8B0000), "Poder oscuro"),
    setOf(2, 4, 5) to Potion("Élixir Dorado",    "⚗️", Color(0xFFFFD700), "¡Rarísima!", isRare = true),
    setOf(1, 2, 3) to Potion("Niebla Venenosa",  "💀", Color(0xFF6B8E23), "Muerte lenta"),
)

fun getPotion(selected: List<Int>): Potion? {
    if (selected.size != 3) return null
    return RECIPES[selected.toSet()]
}