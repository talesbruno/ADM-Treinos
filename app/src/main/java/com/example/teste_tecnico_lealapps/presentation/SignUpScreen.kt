package com.example.teste_tecnico_lealapps.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel
import com.example.teste_tecnico_lealapps.ui.theme.TestetecnicoLealAppsTheme
import com.example.teste_tecnico_lealapps.utils.Result
import com.example.teste_tecnico_lealapps.utils.SignInFormEvent

@Composable
fun SignUpScreen(
    signInViewModel: SignInViewModel,
    modifier: Modifier = Modifier,
    onNavigateToHomeAccount: () -> Unit,
) {
    val state = signInViewModel.stateSignUp
    val snackbarHostState = remember { SnackbarHostState() }
    val signInState by signInViewModel.isUserAuthenticated.collectAsStateWithLifecycle()
    LaunchedEffect(signInState) {
        if (signInState.isAuthenticated) {
            onNavigateToHomeAccount()
        }
    }
    val loading by signInViewModel.isLoading.collectAsStateWithLifecycle()
    if (loading) {
        SplashScreen()
    }
    val error by signInViewModel.isError.collectAsStateWithLifecycle()
    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            val result = snackbarHostState.showSnackbar(
                error, "Ok"
            )
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crie sua Conta preechendo os campos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = { signInViewModel.onEventSignUp(SignInFormEvent.EmailChanged(it)) },
                label = { Text(text = "E-mail") },
                isError = state.emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if (state.emailError != null) {
                Text(
                    text = state.emailError,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            OutlinedTextField(
                value = state.password,
                onValueChange = { signInViewModel.onEventSignUp(SignInFormEvent.PasswordChanged(it)) },
                label = { Text(text = "Password") },
                isError = state.passwordError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = { signInViewModel.onEventSignUp(SignInFormEvent.Submit) }
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Cadastrar")
            }
        }
    }
}