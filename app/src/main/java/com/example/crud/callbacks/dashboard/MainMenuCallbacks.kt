package com.example.crud.callbacks.dashboard

import com.example.crud.model.ErrorResponse
import com.example.crud.model.dashboard.DashboardMainMenuResponse

interface MainMenuCallbacks {
    fun onResponse(data: DashboardMainMenuResponse)
    fun onError(data: ErrorResponse)
}