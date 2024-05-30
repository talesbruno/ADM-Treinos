package com.example.teste_tecnico_lealapps.utils

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
){
    class Initial<T>(): Result<T>()
    class Success<T>(message: String, data: T? = null) : Result<T>(data, message)
    class Loading<T>(data: T? = null) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}