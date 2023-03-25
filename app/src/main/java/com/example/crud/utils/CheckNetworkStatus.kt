package com.example.crud.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

object CheckNetworkStatus {

    fun isOnline(context: Context?, statusCallBack:Status){
        var netWorkStatus = false
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           cm.activeNetwork
        }else{
            //do nothing
        }
    }
    interface Status{
        fun online()
        fun offline()
    }
}