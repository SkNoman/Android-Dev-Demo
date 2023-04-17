package com.example.crud.viewmodel.menus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud.callbacks.menu.GetAllCarListCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.menu.AllCarListResponse
import com.example.crud.repository.menus.CarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(private val carsRepository: CarsRepository)
    :ViewModel(),GetAllCarListCallback{

     val allCarList = MutableLiveData<AllCarListResponse>()
     val errorResponse = MutableLiveData<ErrorResponse>()

    fun getAllCarList(url: String){
        carsRepository.getAllCarList(url,this)
    }
    override fun onResponse(data: AllCarListResponse) {
        allCarList.postValue(data)
    }

    override fun onError(data: ErrorResponse) {
        errorResponse.postValue(data)
    }
}