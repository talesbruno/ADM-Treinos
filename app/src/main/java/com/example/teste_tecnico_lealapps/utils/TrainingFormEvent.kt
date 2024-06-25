package com.example.teste_tecnico_lealapps.utils

sealed class TrainingFormEvent {
    data class NameChanged(val name: String) : TrainingFormEvent()
    data class DescriptionChanged(val description: String) : TrainingFormEvent()
    data class DateChanged(val date: String) : TrainingFormEvent()
    data class TimeChanged(val time: String) : TrainingFormEvent()
    object Submit: TrainingFormEvent()
}