package com.example.teste_tecnico_lealapps.domain.model

import android.net.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ExerciseDto(
    val uuid: String? = null,
    val trainingUuid: String? = null,
    val name: String,
    val imageUri: String,
    val description: String
) {
    constructor() : this("", "", "", "", "")

}
