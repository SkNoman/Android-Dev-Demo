package com.example.crud.network

import com.example.crud.model.GetBaseUrlResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface APIInterface {
    @GET()
    fun getBaseUrl(
        @Url url:String,
        @Header("X-RequestHash") requestHash:String
    ): Call<GetBaseUrlResponse>

    @GET()
    fun getDemoData(
        @Url url:String
    ):Call<ResponseBody>
}