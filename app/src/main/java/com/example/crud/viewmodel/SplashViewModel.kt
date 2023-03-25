package com.example.crud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.callbacks.GetDemoDataCallback
import com.example.crud.repository.GetDemoDataRepository
import com.example.crud.utils.ApiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getDemoDataRepo: GetDemoDataRepository):
ViewModel(),GetDemoDataCallback{

    var demoLiveData = MutableLiveData<ResponseBody>()
    var errorResponse = MutableLiveData<ApiEvent<String>>()

    fun getBaseUrl(url:String){
        viewModelScope.launch (Dispatchers.IO){
            getDemoDataRepo.getDemoData(url,this@SplashViewModel)
        }
    }

    override fun onDemoDataResponse(data: ResponseBody) {
        demoLiveData.value = data
    }

    override fun onDemoDataError(message: String) {
        errorResponse.value = ApiEvent(message)
    }

}