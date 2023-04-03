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
import com.example.crud.utils.SharedPref
import com.example.crud.viewmodel.DashboardViewModel
import com.example.crud.viewmodel.DemoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUserDashboard : BaseFragmentWithBinding<FragmentUserDashboardBinding>
    (FragmentUserDashboardBinding:: inflate) {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val demoViewModel: DemoViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val localDbVersion = SharedPref.getData(requireContext()).getInt("dbVersion",0)
        binding.btnDelete.setOnClickListener{
            dashboardViewModel.deleteDashboardMainMenuFromLocalDB()
        }

        try {
            demoViewModel.getDemoData(BuildConfig.BASE_URL+APIEndpoint.GET_LOCAL_DB_INFO)
            demoViewModel.demoLiveData.observe(viewLifecycleOwner) {
                val response = it.string()
                val regex = Regex(":([0-9]+)")
                val matchResult = regex.find(response)
                if (matchResult != null) {
                    val dbVersion = matchResult.groupValues[1].toInt()
                    Log.e("db-versions","local: $localDbVersion, remote: $dbVersion")
                    SharedPref.sharedPrefManger(requireContext(),dbVersion,"dbVersion")
                    if (dbVersion>localDbVersion){
                        fetchMenuFromRemote()
                    }else{
                        fetchMenuFromLocal()
                    }
                }
            }
        }catch (e:Exception){
            Log.e("nlog-fetch-db-version",e.toString())
        }
    }

    private fun fetchMenuFromLocal() {
        dashboardViewModel.getDashboardMainMenuFromLocalDB.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Log.e("nlog-local-menus","Not empty")
                showMenus(it)
            }
        }
    }

    private fun fetchMenuFromRemote() {
        Log.e("nlog-is-enter-remote","yes")
        try {
            dashboardViewModel.getMainManuList(BuildConfig.BASE_URL+APIEndpoint.MAIN_MENU)

            dashboardViewModel.mainMenuListLiveData.observe(viewLifecycleOwner) {
                if (it.menus!!.isNotEmpty()){
                    try {

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