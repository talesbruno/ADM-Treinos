package com.example.teste_tecnico_lealapps.presentation.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teste_tecnico_lealapps.domain.model.SignInFormState
import com.example.teste_tecnico_lealapps.domain.model.SignUpFormState
import com.example.teste_tecnico_lealapps.domain.model.User
import com.example.teste_tecnico_lealapps.domain.model.UserDto
import com.example.teste_tecnico_lealapps.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import com.example.teste_tecnico_lealapps.utils.Result
import com.example.teste_tecnico_lealapps.utils.SignInFormEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(SignInFormState())
    var stateSignUp by mutableStateOf(SignUpFormState())

    private val _signUpResult = MutableStateFlow<Result<FirebaseUser>>(Result.Initial())
    val signUpResult: StateFlow<Result<FirebaseUser>> = _signUpResult

    private val _signInResult = MutableStateFlow<Result<FirebaseUser>>(Result.Initial())
    val signInResult: StateFlow<Result<FirebaseUser>> = _signInResult

    private val _isUserAuthenticated = MutableStateFlow(UserDto())
    val isUserAuthenticated: StateFlow<UserDto> = _isUserAuthenticated

    init {
        checkUserOnlineStatus()
    }

    private fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signUp(email, password)
                .collect { result ->
                    _signUpResult.value = result
                }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signIn(email, password)
                .collect { result ->
                    _signInResult.value = result
                    if (result is Result.Success) {
                        val user = result.data
                        _isUserAuthenticated.value = user?.email?.let { UserDto(true, it) }!!
                    }
                }
        }
    }

    fun logout() {
        authRepository.logout()
        _isUserAuthenticated.value = UserDto(false, "")
    }

    private fun checkUserOnlineStatus() {
        viewModelScope.launch {
            authRepository.isUserOnline()
                .collect { result ->
                    if (result is Result.Success) {
                        val user = result.data
                        _isUserAuthenticated.value = user?.email?.let { UserDto(true, it) }!!
                    }
                }
        }
    }

    fun onEvent(event: SignInFormEvent) {
        when (event) {
            is SignInFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is SignInFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is SignInFormEvent.Submit -> {
                submitSignIn()
            }
        }
    }

    fun onEventSignUp(event: SignInFormEvent) {
        when (event) {
            is SignInFormEvent.EmailChanged -> {
                stateSignUp = stateSignUp.copy(email = event.email)
            }

            is SignInFormEvent.PasswordChanged -> {
                stateSignUp = stateSignUp.copy(password = event.password)
            }

            is SignInFormEvent.Submit -> {
                submitSignUp()
            }
        }
    }

    private fun submitSignIn() {
        val emailResult = validateEmail(state.email)
        val passwordResult = validatePassword(state.password)
        if (emailResult && passwordResult) {
            signIn(state.email, state.password)
        }
    }

    private fun submitSignUp() {
        val emailResult = validateEmail(stateSignUp.email)
        val passwordResult = validatePassword(stateSignUp.password)
        if (emailResult && passwordResult) {
            signUp(stateSignUp.email, stateSignUp.password)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(emailError = "Email inválido")
            stateSignUp = stateSignUp.copy(emailError = "Email inválido")
            false
        } else {
            state = state.copy(emailError = null)
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.length < 6) {
            state = state.copy(passwordError = "Senha deve ter pelo menos 6 caracteres")
            stateSignUp = stateSignUp.copy(passwordError = "Senha deve ter pelo menos 6 caracteres")
            false
        } else {
            state = state.copy(passwordError = null)
            true
        }
    }
}
