package com.example.teste_tecnico_lealapps.domain.model

data class Training(
    val uuid: String? = null,
    val userUuid: String? = null,
    val name: String,
    val description: String,
    val date: String
){
    constructor(): this("", "", "", "", "")
}
