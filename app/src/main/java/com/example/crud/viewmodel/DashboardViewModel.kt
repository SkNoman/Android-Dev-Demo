package com.example.crud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud.callbacks.dashboard.MainMenuCallbacks
import com.example.crud.model.ErrorResponse
import com.example.crud.model.dashboard.DashboardMainMenuResponse
import com.example.crud.model.dashboard.MenusItem
import com.example.crud.repository.dashboard.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val dashboardRepository: DashboardRepository)
    :ViewModel(),MainMenuCallbacks{

    var mainMenuListLiveData = MutableLiveData<DashboardMainMenuResponse>()
    var errorResponse = MutableLiveData<ErrorResponse>()
    val getDashboardMainMenuFromLocalDB: LiveData<List<MenusItem>> =
        dashboardRepository.getAllDashboardMainMenuFromLocalDB



    fun getMainManuList(url:String){
        dashboardRepository.getMainMenuList(url,this)
    }

    fun insertMainMenusToLocalDB(menusItem: List<MenusItem>){
        viewModelScope.launch {
            dashboardRepository.insertDashboardMainMenuToLocalDB(menusItem)
        }
    }
    fun deleteDashboardMainMenuFromLocalDB(){
        viewModelScope.launch {
            dashboardRepository.deleteDashboardMainMenuFromLocalDb()
        }
    }

    override fun onResponse(data: DashboardMainMenuResponse) {
        mainMenuListLiveData.postValue(data)
    }

    override fun onError(data: ErrorResponse) {
        errorResponse.postValue(data)
    }
}