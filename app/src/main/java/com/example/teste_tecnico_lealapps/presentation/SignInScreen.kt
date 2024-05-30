package com.example.teste_tecnico_lealapps.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teste_tecnico_lealapps.Graph
import com.example.teste_tecnico_lealapps.presentation.viewmodels.SignInViewModel
import com.example.teste_tecnico_lealapps.ui.theme.TestetecnicoLealAppsTheme
import com.example.teste_tecnico_lealapps.utils.Result

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    modifier: Modifier = Modifier,
    onNavigateToHomeAccount: () -> Unit,
    onNavigateToPlashAccount: () -> Unit,
    onNavigateToCreateAccount: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signInState by signInViewModel.isUserAuthenticated.collectAsStateWithLifecycle()
    LaunchedEffect(signInState) {
        if (signInState.isAuthenticated) {
            onNavigateToHomeAccount()
        }
    }

Column(
modifier.fillMaxSize(),
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
        value = email,
        onValueChange = { email = it },
        label = { Text(text = "E-mail") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.size(16.dp))
    Button(onClick = { signInViewModel.signIn(email, password) }) {
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
