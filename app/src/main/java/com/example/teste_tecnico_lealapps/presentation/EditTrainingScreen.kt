package com.example.teste_tecnico_lealapps.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teste_tecnico_lealapps.domain.model.Training
import com.example.teste_tecnico_lealapps.presentation.viewmodels.TrainingViewModel
import com.example.teste_tecnico_lealapps.ui.theme.Pink40
import com.example.teste_tecnico_lealapps.ui.theme.Pink80
import com.example.teste_tecnico_lealapps.ui.theme.Purple40
import com.example.teste_tecnico_lealapps.ui.theme.Purple80
import com.example.teste_tecnico_lealapps.ui.theme.PurpleGrey80
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTrainingScreen(
    trainingUuid: String,
    trainingViewModel: TrainingViewModel,
    modifier: Modifier = Modifier,
) {
    val trainingList by trainingViewModel.training.collectAsStateWithLifecycle()
    val findTraining = trainingList.data?.find {
        it.uuid == trainingUuid
    }
    var name by remember { mutableStateOf(findTraining?.name) }
    var description by remember { mutableStateOf(findTraining?.description) }
    var selectedCustomFormattedDate by remember { mutableStateOf(findTraining?.date) }
    var selectedCustomFormattedTime by remember { mutableStateOf("") }
    val customDateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        selectedCustomFormattedDate =
                            customDateFormat.format(datePickerState.selectedDateMillis)
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) { Text("Cancel") }
            },
            colors = DatePickerDefaults.colors(
                containerColor = PurpleGrey80,
            )
        )
        {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = Pink40,
                    todayDateBorderColor = Purple80,
                    selectedDayContentColor = Purple80,
                    dayContentColor = Purple80,
                    selectedDayContainerColor = Purple80,
                )
            )
        }
    }
    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        val selectedTime =
                            LocalTime.of(timePickerState.hour, timePickerState.minute)
                        selectedCustomFormattedTime =
                            selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                    }
                ) { Text("Cancel") }
            },
            containerColor = PurpleGrey80
        )
        {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Purple40,
                    selectorColor = Pink80,
                    containerColor = PurpleGrey80,
                    clockDialUnselectedContentColor = Purple80,
                )
            )
        }
    }
    Column(
        modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Atualizar informações do treino",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))
        name?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { name = it },
                label = { Text(text = "Nome do Treino") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
        description?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { description = it },
                label = { Text(text = "Descrição") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
        selectedCustomFormattedDate?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {},
                label = { Text(text = "Selecionar Data") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date Picker",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                readOnly = true
            )
        }
        OutlinedTextField(
            value = selectedCustomFormattedTime,
            onValueChange = {},
            label = { Text(text = "Selecionar Hora") },
            trailingIcon = {
                IconButton(onClick = { showTimePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time Picker",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            readOnly = true
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = {
                if (findTraining != null) {
                    findTraining.uuid?.let { name?.let { it1 ->
                        description?.let { it2 ->
                            selectedCustomFormattedDate?.let { it3 ->
                                trainingViewModel.updateTraining(it,
                                    it1, it2, it3
                                )
                            }
                        }
                    } }
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Done, contentDescription = null)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = "Atualizar")
        }
    }
}
