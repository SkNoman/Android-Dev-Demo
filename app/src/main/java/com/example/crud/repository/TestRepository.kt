package com.example.crud.repository

import com.example.crud.callbacks.DemoCallback
import com.example.crud.network.APIInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class TestRepository @Inject constructor(private val api: APIInterface) {
    suspend fun getDemoData(
        url:String,
        onCallback: DemoCallback
    ) = coroutineScope { async (Dispatchers.IO){
        api.getDemoData(url).enqueue(object :retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    onCallback.onResponse(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onCallback.onErrorResponse("Something went wrong")
            }

        })
    } }.await()
}