package com.example.crud.ui.dashboard
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.crud.BuildConfig
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentUserDashboardBinding
import com.example.crud.model.dashboard.MenusItem
import com.example.crud.network.APIEndpoint
import com.example.crud.ui.adapters.DashboardMainMenuAdapter
import com.example.crud.viewmodel.DashboardViewModel
import com.example.crud.viewmodel.DemoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUserDashboard : BaseFragmentWithBinding<FragmentUserDashboardBinding>
    (FragmentUserDashboardBinding:: inflate) {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val demoViewModel: DemoViewModel by viewModels()
    private var localDbVersion = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            demoViewModel.getDemoData(BuildConfig.BASE_URL+APIEndpoint.GET_LOCAL_DB_INFO)
            demoViewModel.demoLiveData.observe(viewLifecycleOwner) {
                val response = it.string()
                val regex = Regex(":([0-9]+)")
                val matchResult = regex.find(response)
                if (matchResult != null) {
                    val dbVersion = matchResult.groupValues[1].toInt()
                    Log.e("nlog-local-db-version",dbVersion.toString())
                    localDbVersion = dbVersion
                }

                Log.e("nlog-local-db-info", response)
            }

            dashboardViewModel.getDashboardMainMenuFromLocalDB.observe(viewLifecycleOwner) {
                if (localDbVersion>1){
                    fetchMenuFromRemote()
                }
                else if (it.isNotEmpty()) {
                    Log.e("nlog-local-menu",it.toString())
                    showMenus(it)
                } else {
                    fetchMenuFromRemote()
                }
            }

        }catch (e:Exception){
            Log.e("nlog-fetch-local-menus",e.toString())
        }
    }

    private fun fetchMenuFromRemote() {
        Log.e("nlog-is-enter","yes")
        try {
            dashboardViewModel.getMainManuList(BuildConfig.BASE_URL+APIEndpoint.MAIN_MENU)

            dashboardViewModel.mainMenuListLiveData.observe(viewLifecycleOwner) {
                if (it.menus!!.isNotEmpty()){
                    Log.e("nlog-main-menu",it.menus.toString())
                    try {
                        dashboardViewModel.deleteDashboardMainMenuFromLocalDB(it.menus)
                        dashboardViewModel.insertMainMenusToLocalDB(it.menus)
                    }catch (e:Exception){
                        Log.e("nlog-local-save-exc",e.toString())
                    }
                    showMenus(it.menus)
                }
            }
        }catch (e:Exception){
            Log.e("nlog-fetch-remote-menus",e.toString())
        }
    }

    private fun showMenus(menusItem: List<MenusItem>) {
        binding.recyclerviewMainMenu.layoutManager =
            GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
        binding.recyclerviewMainMenu.adapter = DashboardMainMenuAdapter(requireContext(),menusItem)
    }
}