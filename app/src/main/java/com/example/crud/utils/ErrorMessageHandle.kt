package com.example.crud.utils

import com.example.crud.model.ErrorResponse
import com.google.gson.Gson
import java.io.Reader

object ErrorMessageHandle {
    fun getErrorMessage(errorBody: Reader): ErrorResponse{
        val gson = Gson()
        val errorBodyObject = gson.fromJson(
            errorBody,
            ErrorJson::class.java
        )
        return ErrorResponse(errorBodyObject.resultCode,errorBodyObject.error.message)
    }
}