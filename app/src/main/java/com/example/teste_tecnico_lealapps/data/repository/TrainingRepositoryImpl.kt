package com.example.teste_tecnico_lealapps.data.repository

import android.net.Uri
import com.example.teste_tecnico_lealapps.domain.model.Exercise
import com.example.teste_tecnico_lealapps.domain.model.ExerciseDto
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.utils.Result
import com.example.teste_tecnico_lealapps.domain.repository.TrainingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : TrainingRepository {
    override fun getAllTraining(): Flow<Result<List<Training>>> =
        callbackFlow {
            val trainingCollection = firebaseAuth.currentUser?.let { user ->
                firebaseFirestore
                    .collection("users")
                    .document(user.uid)
                    .collection("training")
            }
            val listenerRegistration = trainingCollection?.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error.message ?: "Unknown error", null))
                } else {
                    val training = snapshot?.documents?.mapNotNull { document ->
                        document.toObject(Training::class.java)
                    }
                    trySend(Result.Success("", training))
                }
            }
            awaitClose {
                listenerRegistration?.remove()
            }
        }

    override suspend fun insertTraining(training: Training): Result<Boolean> = try {
        val userUuid = firebaseAuth.currentUser?.uid
        if (userUuid == null) {
            Result.Error("Usuário não autenticado")
        } else {
            val trainingId = firebaseFirestore.collection("users/$userUuid/training").document().id
            val addTraining = Training(
                trainingId,
                userUuid,
                training.name,
                training.description,
                training.date
            )
            firebaseFirestore.collection("users/$userUuid/training")
                .document(trainingId)
                .set(addTraining)
                .await()

            Result.Success("Treino cadastrado com sucesso!", true)
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override suspend fun deleteTraining(training: Training): Result<Boolean> = try {
        val userUuid = firebaseAuth.currentUser?.uid
        if (userUuid == null) {
            Result.Error("Usuário não autenticado")
        } else {
            training.uuid?.let { deleteExercisesByTraining(userUuid, it) }
            training.uuid?.let {
                firebaseFirestore.collection("users/$userUuid/training")
                    .document(it)
                    .delete()
                    .await()
            }
            Result.Success("Treino excluído com sucesso!", true)
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override suspend fun updateTraining(
        uuid: String,
        name: String,
        description: String,
        date: String
    ): Result<Boolean> = try {
        val userUuid = firebaseAuth.currentUser?.uid
        if (userUuid == null) {
            Result.Error("Usuário não autenticado")
        } else {
            val productData = mutableMapOf<String, Any>(
                "name" to name,
                "description" to description,
                "date" to date
            )
            firebaseFirestore.collection("users/$userUuid/training")
                .document(uuid)
                .update(productData)
                .await()

            Result.Success("Atualizado com sucesso!", true)
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override fun getAllExercises(trainingId: String): Flow<Result<List<ExerciseDto>>> =
        callbackFlow {
            trySend(Result.Loading())

            val userUuid = firebaseAuth.currentUser?.uid
            if (userUuid == null) {
                trySend(Result.Error("Usuário não autenticado"))
                close()
                return@callbackFlow
            }

            // Obtenção da lista de exercícios no Firestore associados ao trainingId fornecido
            firebaseFirestore.collection("users/$userUuid/exercises")
                .whereEqualTo("trainingUuid", trainingId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val allExercises = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(ExerciseDto::class.java)
                    }
                    trySend(Result.Success("", allExercises))
                }
                .addOnFailureListener { exception ->
                    trySend(Result.Error(exception.message.toString()))
                }

            awaitClose()
        }

    override suspend fun insertExercises(exercise: Exercise): Result<Boolean> = try {
        val userUuid = firebaseAuth.currentUser?.uid
        if (userUuid == null) {
            Result.Error("Usuário não autenticado")
        } else {
            val imageRef =
                firebaseStorage.reference.child("images/$userUuid/${System.currentTimeMillis()}")
            val uploadTask = imageRef.putFile(exercise.imageUri)
            uploadTask.await()
            val imageUrl = imageRef.downloadUrl.await()
            val exerciseId = firebaseFirestore.collection("users/$userUuid/exercises").document().id

            val addExercise = Exercise(
                exerciseId,
                exercise.trainingUuid,
                exercise.name,
                imageUrl,
                exercise.description
            )
            firebaseFirestore.collection("users/$userUuid/exercises")
                .document(exerciseId)
                .set(addExercise)
                .await()

            Result.Success("Exercício cadastrado com sucesso!", true)
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override suspend fun deleteExercises(exercise: Exercise): Result<Boolean> = try {
        val userUuid = firebaseAuth.currentUser?.uid
        if (userUuid == null) {
            Result.Error("Usuário não autenticado")
        } else {
            exercise.uuid?.let {
                firebaseFirestore.collection("users/$userUuid/exercises")
                    .document(it)
                    .delete()
                    .await()
            }

            Result.Success("exercise excluído com sucesso!", true)
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override suspend fun updateExercises(
        uuid: String,
        name: String,
        imageUri: Uri,
        description: String
    ): Result<Boolean> = try {
        val userUuid = firebaseAuth.currentUser?.uid
        if (userUuid == null) {
            Result.Error("Usuário não autenticado")
        } else {
            val imageUrl = run {
                val imageRef =
                    firebaseStorage.reference.child("images/$userUuid/${System.currentTimeMillis()}")
                val uploadTask = imageRef.putFile(imageUri)

                uploadTask.await()

                imageRef.downloadUrl.await().toString()
            }

            val exerciseData = mutableMapOf<String, Any>(
                "name" to name,
                "description" to description
            )

            imageUrl.let { exerciseData["imageUri"] = it }

            firebaseFirestore.collection("users/$userUuid/exercises")
                .document(uuid)
                .update(exerciseData)
                .await()

            Result.Success("Atualizado com sucesso!", true)
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    private suspend fun deleteExercisesByTraining(userUuid: String, trainingUuid: String) {
        firebaseFirestore.collection("users/$userUuid/exercises")
            .whereEqualTo("trainingUuid", trainingUuid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.forEach { document ->
                    document.reference.delete()
                }
            }
            .addOnFailureListener { exception ->

            }
            .await()
    }
}

