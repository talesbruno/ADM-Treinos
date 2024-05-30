package com.example.teste_tecnico_lealapps.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.teste_tecnico_lealapps.domain.model.Exercise
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.presentation.viewmodels.TrainingViewModel

@Composable
fun CreateExerciseScreen(
    trainingUuid: String,
    trainingViewModel: TrainingViewModel,
    onPopBackStack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )
    Column(
        modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Adicione um Exercicio",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))
        Image(
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(100.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Nome do Exercício") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Descrição") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        TextButton(onClick = { galleryLauncher.launch("image/*") }) {
            Text(text = "Selecionar uma imagem")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = {
                imageUri?.let { Exercise(null, trainingUuid, name, it, description,) }?.let {
                    trainingViewModel.insertExercise(
                        it
                    )
                }
                onPopBackStack()
            }
        ) {
            Icon(imageVector = Icons.Default.Done, contentDescription = null)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = "Cadastrar")
        }
    }
}