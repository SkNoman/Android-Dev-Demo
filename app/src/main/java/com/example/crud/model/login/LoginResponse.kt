package com.example.crud.model.login

data class LoginResponse(
    val status: Int?,
    val isSuccess: Boolean?,
    val message: String?,
    val token: String?
)
