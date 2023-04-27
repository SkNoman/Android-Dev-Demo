package com.example.crud.repository.menus

import android.util.Log
import com.example.crud.callbacks.menu.GetAllCarListCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.menu.AllCarListResponse
import com.example.crud.network.APIInterface
import com.example.crud.utils.Constant
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class CarsRepository @Inject constructor(private val api: APIInterface){
    fun getAllCarList(url:String,callback: GetAllCarListCallback){
        api.getAllCars(url).enqueue(object :retrofit2.Callback<AllCarListResponse>{
            override fun onResponse(
                call: Call<AllCarListResponse>,
                response: Response<AllCarListResponse>
            ) {
                if (response.isSuccessful){
                    Log.e("nlog-repo",response.body().toString())
                    callback.onResponse(response.body()!!)
                }else{
                    callback.onError(ErrorResponse(500,Constant.ERROR_MESSAGE))
                }
            }

            override fun onFailure(call: Call<AllCarListResponse>, t: Throwable) {
                t.localizedMessage?.let { ErrorResponse(500, it) }
                    ?.let { callback.onError(it) }
            }

        })
    }
}