package com.example.teste_tecnico_lealapps.domain.repository

import android.net.Uri
import com.example.teste_tecnico_lealapps.domain.model.Exercise
import com.example.teste_tecnico_lealapps.domain.model.ExerciseDto
import com.example.teste_tecnico_lealapps.utils.Result
import com.example.teste_tecnico_lealapps.domain.model.Training
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TrainingRepository {
    fun getAllTraining(): Flow<Result<List<Training>>>
    suspend fun insertTraining(raining: Training): Result<Boolean>
    suspend fun deleteTraining(training: Training): Result<Boolean>
    suspend fun updateTraining(
        uuid: String,
        name: String,
        description: String,
        date: String
    ): Result<Boolean>

    fun getAllExercises(trainingId: String): Flow<Result<List<ExerciseDto>>>
    suspend fun insertExercises(exercise: Exercise): Result<Boolean>
    suspend fun deleteExercises(exercise: Exercise): Result<Boolean>
    suspend fun updateExercises(
        uuid: String,
        name: String,
        imageUri: Uri,
        description: String
    ): Result<Boolean>
}