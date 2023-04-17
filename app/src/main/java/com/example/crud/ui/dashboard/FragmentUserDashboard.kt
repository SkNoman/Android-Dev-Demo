package com.example.crud.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.BuildConfig
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentUserDashboardBinding
import com.example.crud.model.dashboard.FeaturedItem
import com.example.crud.model.dashboard.MenusItem
import com.example.crud.network.APIEndpoint
import com.example.crud.ui.adapters.DashboardMainMenuAdapter
import com.example.crud.ui.adapters.FeaturedListItemAdapter
import com.example.crud.ui.adapters.OnClickMenu
import com.example.crud.utils.CheckNetwork
import com.example.crud.utils.SharedPref
import com.example.crud.utils.showCustomToast
import com.example.crud.viewmodel.DashboardViewModel
import com.example.crud.viewmodel.DemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION")
@AndroidEntryPoint
class FragmentUserDashboard : BaseFragmentWithBinding<FragmentUserDashboardBinding>
    (FragmentUserDashboardBinding:: inflate),OnClickMenu {
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val demoViewModel: DemoViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val localDbVersion = SharedPref.getData(requireContext()).getInt("dbVersion",0)
        try {
            if (CheckNetwork(requireContext()).isNetworkConnected){
                demoViewModel.getDemoData(BuildConfig.BASE_URL+APIEndpoint.GET_LOCAL_DB_INFO)
            }else{
                Toast(requireContext()).showCustomToast(getString(R.string.pls_chk_internet)
                    ,requireActivity())
                fetchMenuFromLocal()
            }
            demoViewModel.demoLiveData.observe(viewLifecycleOwner) {
                val response = it.string()
                val regex = Regex(":([0-9]+)")
                val matchResult = regex.find(response)
                if (matchResult != null) {
                    val dbVersion = matchResult.groupValues[1].toInt()
                    SharedPref.sharedPrefManger(requireContext(),dbVersion,"dbVersion")
                    if (dbVersion>localDbVersion){
                        fetchMenuFromRemote()
                    }else{
                        fetchMenuFromLocal()
                    }
                }
            }
        }catch (e:Exception){
            Toast(requireContext()).showCustomToast(e.toString(),requireActivity())
        }
        lifecycleScope.launch {
            featuredRecycler()
        }


        binding.bottomNavigation.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.users -> {
                    findNavController().navigate(R.id.fragmentLogin)
                }
                R.id.item2 -> {
                    findNavController().navigate(R.id.users)
                }
                R.id.item3 ->{
                    findNavController().navigate(R.id.fragmentCars)
                }

            }
        }

    }
    private  fun featuredRecycler() {
        binding.featuredRecyclerView.setHasFixedSize(true)
        binding.featuredRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val featuredLocations: ArrayList<FeaturedItem> = ArrayList()
        //MANUAL ADDITION OF RECYCLER CARD VIEW
        featuredLocations.add(
            FeaturedItem(
                R.drawable.google,
                "Google",
                "The best search engine"
            )
        )
        featuredLocations.add(
            FeaturedItem(
                R.drawable.facebook,
                "Facebook",
                "Best social media platform"
            )
        )
        featuredLocations.add(
            FeaturedItem(
                R.drawable.linkedin,
                "Linkedin",
                "Best professional media"
            )
        )
        binding.featuredRecyclerView.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.featuredRecyclerView.adapter =
            FeaturedListItemAdapter(requireContext(),featuredLocations)
    }



    private fun fetchMenuFromLocal() {
        dashboardViewModel.getDashboardMainMenuFromLocalDB.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showMenus(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun fetchMenuFromRemote() {
        try {
            if(CheckNetwork(requireContext()).isNetworkConnected){
                dashboardViewModel.getMainManuList(BuildConfig.BASE_URL+APIEndpoint.MAIN_MENU)
            }else{
                Toast(requireContext()).showCustomToast(getString(R.string.pls_chk_internet),requireActivity())
            }
            dashboardViewModel.mainMenuListLiveData.observe(viewLifecycleOwner) {
                if (it.menus!!.isNotEmpty()){
                    try {
                        dashboardViewModel.deleteDashboardMainMenuFromLocalDB()
                        dashboardViewModel.insertMainMenusToLocalDB(it.menus)
                    }catch (e:Exception){
                        Toast(requireContext()).showCustomToast(e.toString(),requireActivity())
                    }
                    showMenus(it.menus)
                }
            }
        }catch (e:Exception){
            Toast(requireContext()).showCustomToast(e.toString(),requireActivity())
        }
    }

    private fun showMenus(menusItem: List<MenusItem>) {
        binding.recyclerviewMainMenu.layoutManager =
            GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
        binding.recyclerviewMainMenu.adapter =
            DashboardMainMenuAdapter(requireContext(),menusItem,this)
    }

    override fun onClick(id: Int) {
        //EACH ID REPRESENTS DIFFERENT FEATURE WHICH IS DEFINED IN DATABASE
        when(id){
            1->{
                findNavController().navigate(R.id.fragmentCars)
            }
            else->{
                Toast.makeText(requireContext(),"This feature is under development",Toast.LENGTH_SHORT).show()
            }
        }
    }
}