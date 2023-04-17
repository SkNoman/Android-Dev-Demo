package com.example.crud.callbacks.menu

import com.example.crud.model.ErrorResponse
import com.example.crud.model.menu.AllCarListResponse

interface GetAllCarListCallback {
    fun onResponse(data:AllCarListResponse)
    fun onError(data: ErrorResponse)
}