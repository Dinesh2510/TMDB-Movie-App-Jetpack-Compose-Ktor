package com.app.movieapp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import kotlinx.coroutines.delay

// --- ANIMATED SPLASH SCREEN COMPOSABLE ---
@Composable
fun SplashScreen(
    onAnimationComplete: () -> Unit // Decoupled Callback for RootNavigation
) {
    val openingScaleX = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "floatingLoop")
    val floatAnimY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 16.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )

    val finalTextColor by animateColorAsState(
        targetValue = if (openingScaleX.value > 0.9f) TmdbCinematicTheme.CoralAccent else TmdbCinematicTheme.TextPrimary,
        animationSpec = tween(1200, delayMillis = 600, easing = FastOutSlowInEasing),
        label = "colorTransition"
    )
    val finalScaleMultiplier by animateFloatAsState(
        targetValue = if (openingScaleX.value > 0.9f) 1.06f else 1f,
        animationSpec = tween(800, delayMillis = 800, easing = FastOutSlowInEasing),
        label = "scaleMultiplier"
    )

    LaunchedEffect(key1 = true) {
        openingScaleX.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )

        delay(3000) // 3 seconds duration

        // Triggers the conditional navigation logic inside RootNavigation
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .scale(openingScaleX.value)
                    .offset(y = floatAnimY.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .size(height = 180.dp, width = 280.dp)
                        .shadow(
                            elevation = 28.dp,
                            shape = RoundedCornerShape(26.dp),
                            ambientColor = Color.Black.copy(alpha = 0.5f),
                            spotColor = Color.Black.copy(alpha = 0.6f)
                        )
                        .clip(RoundedCornerShape(26.dp))
                        .border(
                            width = 1.dp,
                            brush = TmdbCinematicTheme.GlassBorderGradient,
                            shape = RoundedCornerShape(26.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = TmdbCinematicTheme.GlassSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "The Movie App",
                            color = TmdbCinematicTheme.TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "TMDB",
                            color = finalTextColor,
                            fontSize = 62.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.scale(finalScaleMultiplier)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "YOUR MOVIE UNIVERSE",
                            color = TmdbCinematicTheme.TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 850)
@Composable
fun TmdbSplashScreenPreview() {
    SplashScreen(onAnimationComplete = {})
}