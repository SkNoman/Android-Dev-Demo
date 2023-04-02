package com.example.crud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud.callbacks.LoginCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.login.LoginResponse
import com.example.crud.repository.SignInSingUpRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInSignUpViewModel @Inject constructor(private val singUpRepository: SignInSingUpRepository)
    :ViewModel(),LoginCallback{

    var loginResponseLiveData = MutableLiveData<LoginResponse>()
    var errorResponse = MutableLiveData<ErrorResponse>()

    fun signIn(url:String,jsonObject: JsonObject){
        singUpRepository.signIn(url,jsonObject,this)
    }
    override fun onLoginResponse(data: LoginResponse) {
        loginResponseLiveData.value = data
    }

    override fun onError(data: ErrorResponse) {
        errorResponse.value = data
    }
}