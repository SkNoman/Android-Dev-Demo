package com.example.crud.callbacks

import okhttp3.ResponseBody

interface DemoCallback {
    fun onResponse(data: ResponseBody)
    fun onErrorResponse(message: String)
}