package com.example.crud.repository

import com.example.crud.callbacks.GetDemoDataCallback
import com.example.crud.network.APIInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class GetDemoDataRepository @Inject constructor(private val api: APIInterface) {
    fun getDemoData(url:String,callback: GetDemoDataCallback){
        api.getDemoData(url).enqueue(object :retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    callback.onDemoDataResponse(response.body()!!)
                }else{
                    callback.onDemoDataError(response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                 callback.onDemoDataError(t.localizedMessage!!.toString())
            }

        })
    }

}