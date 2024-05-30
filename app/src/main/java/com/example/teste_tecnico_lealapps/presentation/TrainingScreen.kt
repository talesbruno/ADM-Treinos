package com.example.teste_tecnico_lealapps.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.example.teste_tecnico_lealapps.utils.Result
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.teste_tecnico_lealapps.domain.model.ExerciseDto
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.presentation.viewmodels.TrainingViewModel
import kotlinx.coroutines.launch

@Composable
fun TrainingScreen(
    trainingUuid: String,
    onNavigateToCreateExerciseScreen: (String) -> Unit,
    trainingViewModel: TrainingViewModel
) {
    val trainingList by trainingViewModel.training.collectAsStateWithLifecycle()
    val findTraining = trainingList.data?.find {
        it.uuid == trainingUuid
    }
    LaunchedEffect(trainingUuid) {
        trainingViewModel.getAllExercises(trainingUuid)
    }
    val exercisesResult by trainingViewModel.exercises.collectAsStateWithLifecycle()
    when (val result = exercisesResult) {
        is Result.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Result.Success -> {
            val exercises = result.data ?: emptyList()
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    findTraining?.name?.let {
                        Text(
                            text = it,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    findTraining?.description?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontSize = 16.sp
                        )
                    }
                    Divider()
                    Text(
                        text = "Meus Exercícios",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(items = exercises) { exercise ->
                            ExerciseCard(
                                exercise = exercise
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    ExtendedFloatingActionButton(
                        text = { Text("Exercício") },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Adicionar exercício"
                            )
                        },
                        onClick = {
                            onNavigateToCreateExerciseScreen(trainingUuid)
                        }
                    )
                }
            }
        }

        is Result.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Erro ao carregar exercícios")
            }
        }

        is Result.Initial -> {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: ExerciseDto,
) {
    Card(
        onClick = { },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        content = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(exercise.imageUri),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(3.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        exercise.name,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        exercise.description,
                        fontWeight = FontWeight.Light,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}