package com.example.teste_tecnico_lealapps.domain.model

data class TrainingFormState(
    val name: String = "",
    val nameError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val date: String = "",
    val dateError: String? = null,
    val time: String = "",
    val timeError: String? = null,
)
