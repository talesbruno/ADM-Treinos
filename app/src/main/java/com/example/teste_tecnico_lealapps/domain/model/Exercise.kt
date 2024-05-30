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
data class Exercise(
    val uuid: String? = null,
    val trainingUuid: String? = null,
    val name: String,
    @Serializable(with = UriSerializer::class)
    val imageUri: Uri,
    val description: String
) {
    constructor() : this("", "", "", Uri.EMPTY, "")
    object UriSerializer : KSerializer<Uri> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Uri) {
            encoder.encodeString(value.toString())
        }

        override fun deserialize(decoder: Decoder): Uri {
            return Uri.parse(decoder.decodeString())
        }

    }
}
