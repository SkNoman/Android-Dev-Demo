package com.example.crud.network

import com.example.crud.model.dashboard.DashboardMainMenuResponse
import com.example.crud.model.free.Users
import com.example.crud.model.login.LoginResponse
import com.example.crud.model.menu.AllCarListResponse
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @GET()
    fun getWholeUsers(
        @Url url:String,
    ): Call<Users>

    @GET()
    fun getAppStatus(
        @Url url: String,
        @Header ("X-RequestHash") requestHash: String
    ): Call<ResponseBody>

    @GET()
    fun getDemoData(
        @Url url:String,
    ):Call<ResponseBody>

    @POST()
    fun signIn(
        @Url url:String,
        @Body jsonObject: JsonObject
    ):Call<LoginResponse>

    @GET()
    fun getMainMenuList(
        @Url url: String
    ):Call<DashboardMainMenuResponse>

    @GET()
    fun getAllCars(
        @Url url:String
    ):Call<AllCarListResponse>
}