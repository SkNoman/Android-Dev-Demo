package com.example.crud.callbacks

import okhttp3.ResponseBody

interface GetDemoDataCallback {
    fun onDemoDataResponse(data: ResponseBody)
    fun onDemoDataError(message:String)
}