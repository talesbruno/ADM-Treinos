package com.example.teste_tecnico_lealapps.data.repository

import com.example.teste_tecnico_lealapps.domain.model.User
import com.example.teste_tecnico_lealapps.domain.repository.AuthRepository
import com.example.teste_tecnico_lealapps.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String
    ): Flow<Result<FirebaseUser>> = callbackFlow {
        trySend(Result.Loading())
        firebaseFirestore.collection("/users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            if (result != null) {
                                val uuid = result.user?.uid
                                if (uuid == null) {
                                    trySend(Result.Error("Error uuid = null"))
                                } else {
                                    firebaseFirestore.collection("/users")
                                        .document(uuid)
                                        .set(
                                            hashMapOf(
                                                "email" to email,
                                                "uuid" to uuid
                                            )
                                        )
                                        .addOnSuccessListener {
                                            trySend(Result.Success("Online", result.user))
                                        }
                                        .addOnFailureListener { exception ->
                                            trySend(Result.Error(exception.message.toString()))
                                        }
                                }
                            } else {
                                trySend(Result.Error("Error do servidor"))
                            }
                        }
                        .addOnFailureListener { exception ->
                            trySend(Result.Error(exception.message.toString()))
                        }
                } else {
                    trySend(Result.Error("Usuário já cadastrado!!!"))
                }
            }
            .addOnFailureListener { exception ->
                trySend(Result.Error(exception.message.toString()))
            }
        awaitClose()
    }

    override suspend fun signIn(email: String, password: String): Flow<Result<FirebaseUser>> =
        callbackFlow {
            trySend(Result.Loading())
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { auth ->
                    trySend(Result.Success("Online", auth.user))
                }
                .addOnFailureListener { exception ->
                    trySend(Result.Error(exception.message.toString()))
                }
            awaitClose()
        }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun isUserOnline(): Flow<Result<User>> = callbackFlow {
        trySend(Result.Loading())
        if (firebaseAuth.currentUser != null) {
            firebaseFirestore.collection("/users")
                .whereEqualTo("uuid", firebaseAuth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { query ->
                    val userDocument = query.documents.firstOrNull()
                    if (userDocument != null) {
                        val user = userDocument.toObject(User::class.java)
                        trySend(Result.Success("Online", user))
                    } else {
                        trySend(Result.Error("Usuário não encontrado"))
                    }
                }
                .addOnFailureListener { exception ->
                    trySend(Result.Error(exception.message.toString()))
                }
        } else {
            trySend(Result.Error("Offline"))
        }
        awaitClose()
    }
}