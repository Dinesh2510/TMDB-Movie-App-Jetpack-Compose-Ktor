/*
 * Copyright (c) 2026 Dinesh2510
 * File : Color.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-22 15:09
 *
 * Author : Dinesh
 * GitHub : https://github.com/Dinesh2510
 * YouTube : https://www.youtube.com/@pixeldesigndeveloper
 * Website : https://pixeldev.in
 *
 * Copyright (c) 2026 Dinesh. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 */

package com.app.movieapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val md_theme_light_primary = Color(0xFF375CA9)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFD9E2FF)
val md_theme_light_onPrimaryContainer = Color(0xFF001945)
val md_theme_light_secondary = Color(0xFF335CA8)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD8E2FF)
val md_theme_light_onSecondaryContainer = Color(0xFF001A43)
val md_theme_light_tertiary = Color(0xFF355CA8)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFD9E2FF)
val md_theme_light_onTertiaryContainer = Color(0xFF001944)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFEFBFF)
val md_theme_light_onBackground = Color(0xFF001849)
val md_theme_light_surface = Color(0xFFFEFBFF)
val md_theme_light_onSurface = Color(0xFF001849)
val md_theme_light_surfaceVariant = Color(0xFFE1E2EC)
val md_theme_light_onSurfaceVariant = Color(0xFF44464F)
val md_theme_light_outline = Color(0xFF757780)
val md_theme_light_inverseOnSurface = Color(0xFFEEF0FF)
val md_theme_light_inverseSurface = Color(0xFF002B75)
val md_theme_light_inversePrimary = Color(0xFFB0C6FF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF375CA9)
val md_theme_light_outlineVariant = Color(0xFFC5C6D0)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFB0C6FF)
val md_theme_dark_onPrimary = Color(0xFF002D6F)
val md_theme_dark_primaryContainer = Color(0xFF1A438F)
val md_theme_dark_onPrimaryContainer = Color(0xFFD9E2FF)
val md_theme_dark_secondary = Color(0xFFAEC6FF)
val md_theme_dark_onSecondary = Color(0xFF002E6B)
val md_theme_dark_secondaryContainer = Color(0xFF14448F)
val md_theme_dark_onSecondaryContainer = Color(0xFFD8E2FF)
val md_theme_dark_tertiary = Color(0xFFAFC6FF)
val md_theme_dark_onTertiary = Color(0xFF002D6D)
val md_theme_dark_tertiaryContainer = Color(0xFF16448F)
val md_theme_dark_onTertiaryContainer = Color(0xFFD9E2FF)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF001849)
val md_theme_dark_onBackground = Color(0xFFDBE1FF)
val md_theme_dark_surface = Color(0xFF001849)
val md_theme_dark_onSurface = Color(0xFFDBE1FF)
val md_theme_dark_surfaceVariant = Color(0xFF44464F)
val md_theme_dark_onSurfaceVariant = Color(0xFFC5C6D0)
val md_theme_dark_outline = Color(0xFF8F9099)
val md_theme_dark_inverseOnSurface = Color(0xFF001849)
val md_theme_dark_inverseSurface = Color(0xFFDBE1FF)
val md_theme_dark_inversePrimary = Color(0xFF375CA9)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFB0C6FF)
val md_theme_dark_outlineVariant = Color(0xFF44464F)
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Color(0xFF0C1B3A)



// --- THEME DEFINITION ---
object FrostedGlassTheme {
    val ScreenBgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0D0C14), Color(0xFF151322), Color(0xFF0D0C14))
    )

    // Liquid Glass Translucent Surface
    val GlassSurfaceColor = Color(0xFF1E1B2E).copy(alpha = 0.55f)

    // Glass Refraction Border (Top edge highlight fading down)
    val GlassBorderGradient = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.45f),
            Color.White.copy(alpha = 0.08f)
        )
    )

    // Active Tab Gradient Pill (Vibrant Coral/Orange)
    val ActiveGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFF5252), Color(0xFFFF7A00))
    )
    // Continuous Cosmic Background Gradient (Top Indigo -> Mid Purple -> Deep OLED Black)

}

object TmdbTheme {
    // --- Cinematic Neutral & Background Colors ---
    // Deep cosmic colors for OLED-friendly dark mode. These deep indigos
    // provide better depth than plain black.
    val BackgroundDeep = Color(0xFF0C0A11) // Deepest cosmic base
    val BackgroundMedium = Color(0xFF14121E) // For elevation/cards
    val BackgroundLight = Color(0xFF1F1C2C) // Lighter contrast/dividers

    // Text & Content Colors
    val TextPrimary = Color(0xFFFFFFFF) // Full white for titles/pop
    val TextSecondary = Color(0xFF9EA3B0) // Softened grey/blue for details

    // --- Glass & Liquid Colors ---
    // These are the core transparent surfaces and edge refraction borders.
    // Glass surface backdrop
    val GlassSurface = Color.White.copy(alpha = 0.08f)

    // Glass refraction border: Subtle fade to simulate light bending on the edge.
    val GlassBorder = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.30f), // Crisp top highlight
            Color.White.copy(alpha = 0.05f)  // Soft bottom fade
        )
    )

    // --- Accent & Brand Colors ---
    // Vibrant Neon Coral: The main CTA color (e.g., "Watch Trailer" button).
    val PrimaryGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFF5252), // Primary pop
            Color(0xFFFF7A00)  // Coral blend
        )
    )

    // Electric Violet: Secondary highlight for active Genre Chips.
    val AccentGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF7F00FF),
            Color(0xFFE100FF)
        )
    )

    // Cinematic Background Gradient: Deep cosmic colors fade through black.
    val BackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            BackgroundDeep,
            Color(0xFF12101A),
            BackgroundDeep
        )
    )

    // Star Rating Gold
    val RatingGold = Color(0xFFFFD700)

    val AppBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F0E17), // Deep cosmic indigo top
            Color(0xFF161522), // Subtle purple/grey center blend
            Color(0xFF0A0910)  // Deep OLED black at bottom
        )
    )


}
object TmdbCinematicTheme {
    // Continuous Cosmic Background Gradient (Top Indigo -> Mid Purple -> Deep OLED Black)
    val AppBackgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F0E17), Color(0xFF161522), Color(0xFF0A0910))
    )

    // Liquid Glass Container Styling
    val GlassSurface = Color(0xFF1B192B).copy(alpha = 0.65f)
    val GlassBorderGradient = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.40f), Color.White.copy(alpha = 0.08f))
    )

    // Dynamic Action Accent (Used only for the animating Name now)
    val AccentCoral = Color(0xFFFF5252)

    // Cinematic Text Colors
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFA7A7B7)

    val CoralAccent = Color(0xFFFF5252)
    val PrimaryActionGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFF5252), Color(0xFFFF7A00))
    )
}