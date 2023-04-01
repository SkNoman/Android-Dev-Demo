package com.example.crud.callbacks

import com.example.crud.model.ErrorResponse
import com.example.crud.model.free.Users

interface WholeUsersCallback {
    fun onResponse(data: Users)
    fun onErrorResponse(message:ErrorResponse)
}