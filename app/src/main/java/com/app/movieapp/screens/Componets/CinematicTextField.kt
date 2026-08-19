package com.app.movieapp.screens.Componets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.app.movieapp.ui.theme.TmdbCinematicTheme

@Composable
fun CinematicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = TmdbCinematicTheme.TextSecondary) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = TmdbCinematicTheme.GlassSurface,
            unfocusedContainerColor = TmdbCinematicTheme.GlassSurface,
            focusedBorderColor = TmdbCinematicTheme.CoralAccent,
            unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
            focusedTextColor = TmdbCinematicTheme.TextPrimary,
            unfocusedTextColor = TmdbCinematicTheme.TextPrimary
        )
    )
}