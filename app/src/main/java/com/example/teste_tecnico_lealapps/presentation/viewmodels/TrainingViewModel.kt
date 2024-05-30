package com.example.teste_tecnico_lealapps.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teste_tecnico_lealapps.domain.model.Exercise
import com.example.teste_tecnico_lealapps.domain.model.ExerciseDto
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.domain.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.teste_tecnico_lealapps.utils.Result
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {
    private val _training = MutableStateFlow<Result<List<Training>>>(Result.Loading())
    val training: StateFlow<Result<List<Training>>> = _training

    private val _deleteTraining = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val deleteTraining: StateFlow<Result<Boolean>> = _deleteTraining

    private val _insertTraining = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val insertTraining: StateFlow<Result<Boolean>> = _insertTraining

    private val _updateTraining = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val updateTraining: StateFlow<Result<Boolean>> = _updateTraining

    private val _exercises = MutableStateFlow<Result<List<ExerciseDto>>>(Result.Loading())
    val exercises: StateFlow<Result<List<ExerciseDto>>> = _exercises

    private val _deleteExercise = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val deleteExercise: StateFlow<Result<Boolean>> = _deleteExercise

    private val _insertExercise = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val insertExercise: StateFlow<Result<Boolean>> = _insertExercise

    private val _updateExercise = MutableStateFlow<Result<Boolean>>(Result.Loading())
    val updateExercise: StateFlow<Result<Boolean>> = _updateExercise

    fun getAllTraining() {
        viewModelScope.launch {
            trainingRepository.getAllTraining().collect {
                _training.value = it
            }
        }
    }

    fun deleteTraining(training: Training) {
        viewModelScope.launch {
            _deleteTraining.value = trainingRepository.deleteTraining(training)
        }
    }

    fun insertTraining(training: Training) {
        viewModelScope.launch {
            _insertTraining.value = trainingRepository.insertTraining(training)
        }
    }

    fun updateTraining(uuid: String, name: String, description: String, date: String) {
        viewModelScope.launch {
            _updateTraining.value = trainingRepository.updateTraining(uuid, name, description, date)
        }
    }

    fun getAllExercises(trainingId: String) {
        viewModelScope.launch {
            trainingRepository.getAllExercises(trainingId).collect {
                _exercises.value = it
            }
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            _deleteTraining.value = trainingRepository.deleteExercises(exercise)
        }
    }

    fun insertExercise(exercise: Exercise) {
        viewModelScope.launch {
            _insertExercise.value = trainingRepository.insertExercises(exercise)
        }
    }

    fun updateExercise(uuid: String, name: String, imageUri: Uri, description: String) {
        viewModelScope.launch {
            _updateExercise.value =
                trainingRepository.updateExercises(uuid, name, imageUri, description)
        }
    }
}