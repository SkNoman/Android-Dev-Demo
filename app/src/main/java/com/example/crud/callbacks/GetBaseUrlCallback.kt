package com.example.crud.callbacks

import com.example.crud.model.ErrorResponse
import com.example.crud.model.GetBaseUrlResponse

interface GetBaseUrlCallback {
    fun onBaseUrlResponse(data: GetBaseUrlResponse)
    fun onBaseUrlError(message:ErrorResponse)
}