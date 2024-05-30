package com.example.teste_tecnico_lealapps.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teste_tecnico_lealapps.domain.model.User
import com.example.teste_tecnico_lealapps.domain.model.UserDto
import com.example.teste_tecnico_lealapps.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import com.example.teste_tecnico_lealapps.utils.Result
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

    private val _signUpResult = MutableStateFlow<Result<FirebaseUser>>(Result.Initial())
    val signUpResult: StateFlow<Result<FirebaseUser>> = _signUpResult

    private val _signInResult = MutableStateFlow<Result<FirebaseUser>>(Result.Initial())
    val signInResult: StateFlow<Result<FirebaseUser>> = _signInResult

    private val _isUserAuthenticated = MutableStateFlow(UserDto())
    val isUserAuthenticated: StateFlow<UserDto> = _isUserAuthenticated

    init {
        checkUserOnlineStatus()
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signUp(email, password)
                .collect { result ->
                    _signUpResult.value = result
                }
        }
    }

    fun signIn(email: String, password: String) {
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
}
