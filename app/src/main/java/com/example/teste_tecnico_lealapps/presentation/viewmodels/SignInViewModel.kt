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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var stateSignIn by mutableStateOf(SignInFormState())
    var stateSignUp by mutableStateOf(SignUpFormState())

    private val _isUserAuthenticated = MutableStateFlow(UserDto())
    val isUserAuthenticated: StateFlow<UserDto> = _isUserAuthenticated

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow("")
    val isError = _isError.asStateFlow()

    init {
        checkUserOnlineStatus()
    }

    private fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signUp(email, password)
                .collect { result ->
                    when (result){
                        is Result.Error -> {
                            val error = result.message
                            _isLoading.value = false
                            if (error != null) {
                                _isError.value = error
                            }
                        }
                        is Result.Initial -> {

                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                        is Result.Success -> {
                            val user = result.data
                            _isLoading.value = false
                            _isUserAuthenticated.value = user?.email?.let { UserDto(true, it) }!!
                        }
                    }
                }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signIn(email, password)
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            val error = result.message
                            _isLoading.value = false
                            if (error != null) {
                                _isError.value = error
                            }
                        }
                        is Result.Initial -> {

                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                        is Result.Success -> {
                            val user = result.data
                            _isLoading.value = false
                            _isUserAuthenticated.value = user?.email?.let { UserDto(true, it) }!!
                        }
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
                stateSignIn = stateSignIn.copy(email = event.email)
            }

            is SignInFormEvent.PasswordChanged -> {
                stateSignIn = stateSignIn.copy(password = event.password)
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
        val emailResult = validateEmail(stateSignIn.email)
        val passwordResult = validatePassword(stateSignIn.password)
        if (emailResult && passwordResult) {
            stateSignIn = stateSignIn.copy(
                emailError = null,
                passwordError = null
            )
            signIn(stateSignIn.email, stateSignIn.password)
        } else {
            stateSignIn = stateSignIn.copy(
                emailError = "Email inválido",
                passwordError = "Senha deve ter pelo menos 6 caracteres"
            )
        }
    }

    private fun submitSignUp() {
        val emailResult = validateEmail(stateSignUp.email)
        val passwordResult = validatePassword(stateSignUp.password)
        if (emailResult && passwordResult) {
            stateSignUp = stateSignUp.copy(
                emailError = null,
                passwordError = null
            )
            signUp(stateSignUp.email, stateSignUp.password)
        } else {
            stateSignUp = stateSignUp.copy(
                emailError = "Email inválido",
                passwordError = "Senha deve ter pelo menos 6 caracteres"
            )
        }
    }

    private fun validateEmail(email: String): Boolean {
        return !(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }
}
