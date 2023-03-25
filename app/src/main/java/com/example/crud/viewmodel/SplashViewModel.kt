package com.example.crud.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.callbacks.GetBaseUrlCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.GetBaseUrlResponse
import com.example.crud.repository.GetBaseUrlRepository
import com.example.crud.utils.ApiEvent
import com.example.crud.utils.ApiSecurity
import com.example.crud.utils.Constant.stringSize
import com.example.crud.utils.RequestHash
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getBaseUrlRepository: GetBaseUrlRepository):
ViewModel(),GetBaseUrlCallback{

    var baseUrlLiveData = MutableLiveData<GetBaseUrlResponse>()
    var baseUrlErrorResponse = MutableLiveData<ApiEvent<ErrorResponse>>()

    fun getBaseUrl(url:String){
        viewModelScope.launch (Dispatchers.IO){
            Log.e("nlog_thread_view_model", Thread.currentThread().toString())
            val getRandomString = ApiSecurity.getRandomString(stringSize)
            val requestHash = RequestHash.create(getRandomString,ApiSecurity.createSign(getRandomString).toString())
            getBaseUrlRepository.getBaseUrl(url,requestHash,this@SplashViewModel)
        }
    }

    override fun onBaseUrlResponse(data: GetBaseUrlResponse) {
        baseUrlLiveData.value = data
    }

    override fun onBaseUrlError(message: ErrorResponse) {
        Log.e("nlog","entered3")
        baseUrlErrorResponse.value = ApiEvent(message)
    }

}