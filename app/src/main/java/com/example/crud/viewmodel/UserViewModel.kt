package com.example.crud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.callbacks.WholeUsersCallback
import com.example.crud.model.ErrorResponse
import com.example.crud.model.free.Users
import com.example.crud.repository.UsersRepository
import com.example.crud.utils.ApiEvent
import com.example.crud.utils.ApiSecurity
import com.example.crud.utils.Constant.stringSize
import com.example.crud.utils.RequestHash
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val usersRepository: UsersRepository):
ViewModel(),WholeUsersCallback{

    var usersLiveData = MutableLiveData<Users>()
    var errorResponse = MutableLiveData<ApiEvent<ErrorResponse>>()

    fun getWholeUsers(url:String){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                val getRandomString = ApiSecurity.getRandomString(stringSize)
                val requestHash = RequestHash.create(getRandomString,ApiSecurity.createSign(getRandomString).toString())
                usersRepository.getWholeUsers(url,this@UserViewModel)
            }
        }
    }

    override fun onResponse(data: Users) {
        usersLiveData.value = data
    }

    override fun onErrorResponse(message: ErrorResponse) {
        errorResponse.value = ApiEvent(message)
    }

}