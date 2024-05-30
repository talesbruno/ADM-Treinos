package com.example.teste_tecnico_lealapps.domain.repository

import com.example.teste_tecnico_lealapps.domain.model.User
import com.example.teste_tecnico_lealapps.utils.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String
    ): Flow<Result<FirebaseUser>>

    suspend fun signIn(
        email: String,
        password: String
    ): Flow<Result<FirebaseUser>>

    fun logout()

    suspend fun isUserOnline(): Flow<Result<User>>
}