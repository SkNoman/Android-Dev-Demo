package com.example.crud.repository


import com.example.crud.callbacks.LoginCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.login.LoginResponse

import com.example.crud.network.APIInterface
import com.example.crud.utils.Constant

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

import javax.inject.Inject


class SignInSingUpRepository @Inject constructor(private val api: APIInterface) {
    fun signIn(url:String,jsonObject: JsonObject,onCallback: LoginCallback){
       api.signIn(url,jsonObject).enqueue(object : retrofit2.Callback<LoginResponse>{
           override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
               if (response.isSuccessful){
                   onCallback.onLoginResponse(response.body()!!)
               }
           }

           override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
               onCallback.onError(ErrorResponse(
                   500,
                   Constant.ERROR_MESSAGE
               ))
           }

       })
    }
}