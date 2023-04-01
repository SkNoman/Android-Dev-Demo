package com.example.crud.repository

import com.example.crud.callbacks.WholeUsersCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.free.Users
import com.example.crud.network.APIInterface
import com.example.crud.utils.Constant
import com.example.crud.utils.ErrorMessageHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class UsersRepository @Inject constructor(private val api: APIInterface) {
    suspend fun getWholeUsers(
        url: String,
        onCallback: WholeUsersCallback
    ) = coroutineScope { async (Dispatchers.IO){
        api.getWholeUsers(url).enqueue(object : retrofit2.Callback<Users>{
            override fun onResponse(
                call: Call<Users>,
                response: Response<Users>
            ) {
                if (response.isSuccessful){
                    onCallback.onResponse(response.body()!!)
                }else{
                    try {
                        onCallback.onErrorResponse(ErrorMessageHandle.getErrorMessage(
                            response.errorBody()!!.charStream()))
                    }catch (e:Exception){
                        onCallback.onErrorResponse(
                            ErrorResponse(
                                -1,
                                Constant.ERROR_MESSAGE
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                onCallback.onErrorResponse(ErrorResponse(
                    -1,
                    Constant.ERROR_MESSAGE
                ))
            }

        })
    } }.await()

}