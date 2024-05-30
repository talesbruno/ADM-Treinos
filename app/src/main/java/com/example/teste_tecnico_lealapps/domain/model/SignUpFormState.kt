package com.example.teste_tecnico_lealapps.domain.model

data class SignUpFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
)
