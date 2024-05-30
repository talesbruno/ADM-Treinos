package com.example.teste_tecnico_lealapps.utils

sealed class SignInFormEvent {
    data class EmailChanged(val email: String) : SignInFormEvent()
    data class PasswordChanged(val password: String) : SignInFormEvent()
    object Submit: SignInFormEvent()
}