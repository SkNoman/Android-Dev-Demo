package com.example.crud.repository.dashboard

import androidx.lifecycle.LiveData
import com.example.crud.callbacks.dashboard.MainMenuCallbacks
import com.example.crud.local.dao.DashboardDao
import com.example.crud.model.ErrorResponse
import com.example.crud.model.dashboard.DashboardMainMenuResponse
import com.example.crud.model.dashboard.MenusItem
import com.example.crud.network.APIInterface
import com.example.crud.utils.Constant
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val api: APIInterface,private val dao: DashboardDao) {
    fun getMainMenuList(url:String,callback: MainMenuCallbacks){
        api.getMainMenuList(url).enqueue(object: retrofit2.Callback<DashboardMainMenuResponse>{
            override fun onResponse(
                call: Call<DashboardMainMenuResponse>,
                response: Response<DashboardMainMenuResponse>
            ) {
                if (response.isSuccessful){
                    callback.onResponse(response.body()!!)
                }else{
                    callback.onError(ErrorResponse(500, Constant.ERROR_MESSAGE))
                }
            }

            override fun onFailure(call: Call<DashboardMainMenuResponse>, t: Throwable) {
                callback.onError(ErrorResponse(500,Constant.ERROR_MESSAGE))
            }

        })
    }

    val getAllDashboardMainMenuFromLocalDB: LiveData<List<MenusItem>> = dao.getAllDashboardMenuFromLocal()

    suspend fun insertDashboardMainMenuToLocalDB(menus: List<MenusItem>){
        dao.saveDashboardMenus(menus)
    }

    suspend fun deleteDashboardMainMenuFromLocalDb(){
        dao.deleteAllDashboardMenuFromLocal()
    }
}