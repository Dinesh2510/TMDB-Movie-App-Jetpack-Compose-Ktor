package com.app.movieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.movieapp.data.viewmodel.AuthViewModel
import com.app.movieapp.screens.Componets.CinematicTextField
import com.app.movieapp.ui.theme.TmdbCinematicTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(TmdbCinematicTheme.AppBackgroundGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Create Account",
                color = TmdbCinematicTheme.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign up to start streaming your favorite movies.",
                color = TmdbCinematicTheme.TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            CinematicTextField(value = name, onValueChange = { name = it; errorMessage = null }, label = "Full Name")
            Spacer(modifier = Modifier.height(16.dp))
            CinematicTextField(value = email, onValueChange = { email = it; errorMessage = null }, label = "Email Address")
            Spacer(modifier = Modifier.height(16.dp))
            CinematicTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = "Password",
                isPassword = true
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = TmdbCinematicTheme.CoralAccent, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(TmdbCinematicTheme.PrimaryActionGradient)
                    .clickable {
                        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                            viewModel.register(name, email, password, onRegisterSuccess)
                        } else {
                            errorMessage = "Please fill in all fields."
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account? ", color = TmdbCinematicTheme.TextSecondary, fontSize = 14.sp)
                Text(
                    text = "Login",
                    color = TmdbCinematicTheme.CoralAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}