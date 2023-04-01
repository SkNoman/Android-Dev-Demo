package com.example.crud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.callbacks.DemoCallback
import com.example.crud.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor( private val testRepository: TestRepository):
ViewModel(),DemoCallback{
    var demoLiveData = MutableLiveData<ResponseBody>()
    var errorResponse = MutableLiveData<String>()

    fun getDemoData(url:String){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                testRepository.getDemoData(url,this@DemoViewModel)
        } }
    }
    override fun onResponse(data: ResponseBody) {
        demoLiveData.postValue(data)
    }

    override fun onErrorResponse(message: String) {
        errorResponse.postValue(message)
    }
}