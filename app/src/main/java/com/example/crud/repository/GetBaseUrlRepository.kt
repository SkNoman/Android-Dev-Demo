package com.example.crud.repository

import android.util.Log
import com.example.crud.callbacks.GetBaseUrlCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.GetBaseUrlResponse
import com.example.crud.network.APIInterface
import com.example.crud.utils.Constant
import com.example.crud.utils.ErrorMessageHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class GetBaseUrlRepository @Inject constructor(private val api: APIInterface) {
    suspend fun getBaseUrl(
        url: String,
        requestHash: String,
        onCallback: GetBaseUrlCallback
    ) = coroutineScope { async (Dispatchers.IO){
        api.getBaseUrl(url,requestHash).enqueue(object : retrofit2.Callback<GetBaseUrlResponse>{
            override fun onResponse(
                call: Call<GetBaseUrlResponse>,
                response: Response<GetBaseUrlResponse>
            ) {
                if (response.isSuccessful){
                    Log.e("nlog_thread", Thread.currentThread().toString())
                    onCallback.onBaseUrlResponse(response.body()!!)
                }else{
                    try {
                        onCallback.onBaseUrlError(ErrorMessageHandle.getErrorMessage(
                            response.errorBody()!!.charStream()))
                    }catch (e:Exception){
                        onCallback.onBaseUrlError(
                            ErrorResponse(
                                -1,
                                Constant.ERROR_MESSAGE
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<GetBaseUrlResponse>, t: Throwable) {
                onCallback.onBaseUrlError(ErrorResponse(
                    -1,
                    Constant.ERROR_MESSAGE
                ))
            }

        })
    } }.await()

}