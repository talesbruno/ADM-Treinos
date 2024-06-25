package com.example.teste_tecnico_lealapps.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel
import com.example.teste_tecnico_lealapps.utils.SignInFormEvent

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    modifier: Modifier = Modifier,
    onNavigateToHomeAccount: () -> Unit,
    onNavigateToPlashAccount: () -> Unit,
    onNavigateToCreateAccount: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = signInViewModel.stateSignIn
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
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Seja Bem-vindo",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = { signInViewModel.onEvent(SignInFormEvent.EmailChanged(it)) },
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
                onValueChange = { signInViewModel.onEvent(SignInFormEvent.PasswordChanged(it)) },
                isError = state.passwordError != null,
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = { signInViewModel.onEvent(SignInFormEvent.Submit) }) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Não tem uma conta?")
            Text(
                text = "Criar conta",
                modifier = Modifier.clickable { onNavigateToCreateAccount() },
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
