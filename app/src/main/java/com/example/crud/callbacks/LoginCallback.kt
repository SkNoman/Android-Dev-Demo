package com.example.crud.callbacks

import com.example.crud.model.ErrorResponse
import com.example.crud.model.login.LoginResponse

interface LoginCallback {
    fun onLoginResponse(data:LoginResponse)
    fun onError(data: ErrorResponse)
}