package com.example.crud.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class FragmentUserDashboard : BaseFragmentWithBinding<FragmentUserDashboardBinding>
    (FragmentUserDashboardBinding:: inflate),OnClickMenu {
    private val timer = Timer()
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val demoViewModel: DemoViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val localDbVersion = SharedPref.getData(requireContext()).getInt("dbVersion",0)
        try {
            if (CheckNetwork(requireContext()).isNetworkConnected){
                Log.e("nlog-net","Yes")
                demoViewModel.getDemoData(BuildConfig.BASE_URL+APIEndpoint.GET_LOCAL_DB_INFO)
            }else{
                Toast(requireContext()).showCustomToast(getString(R.string.pls_chk_internet)
                    ,requireActivity())
                fetchMenuFromLocal()
            }
            //fetchMenuFromLocal()
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
            Toast(requireContext()).showCustomToast(e.toString(),requireActivity())
        }
        lifecycleScope.launch {
            featuredRecycler()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel() // Cancel the Timer to stop auto scrolling
    }


    private suspend fun featuredRecycler() {
        binding.featuredRecyclerView.setHasFixedSize(true)
        binding.featuredRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val featuredLocations: ArrayList<FeaturedItem> = ArrayList()
        featuredLocations.add(
            FeaturedItem(
                R.drawable.google,
                "Home Automation",
                "We can automate your home"
            )
        ) // manual addition of recycler card view
        featuredLocations.add(
            FeaturedItem(
                R.drawable.facebook,
                "Plumber",
                "We always serve you skilled person"
            )
        )
        featuredLocations.add(
            FeaturedItem(
                R.drawable.linkedin,
                "Ac Mechanic",
                "Our skilled mechanic can deal with any problem with your Ac"
            )
        )
        binding.featuredRecyclerView.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.featuredRecyclerView.adapter =
            FeaturedListItemAdapter(requireContext(),featuredLocations)

        withContext(Dispatchers.IO){
            val delay = 2000L // Delay in milliseconds before the first scroll
            val interval = 3000L // Interval in milliseconds between subsequent scrolls

            timer.schedule(object : TimerTask() {

                override fun run() {
                    // Get the current position of the RecyclerView
                    val currentPosition = (binding.featuredRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    // Calculate the next position to scroll to
                    val nextPosition = if (currentPosition < (binding.featuredRecyclerView.adapter?.itemCount
                            ?: (0 - 2))
                    ) {
                        currentPosition + 1
                    } else { 0 }
                    // Scroll to the next position
                    activity?.runOnUiThread {
                        // Scroll to the next position
                        binding.featuredRecyclerView.scrollToPosition(nextPosition)
                        // If the next position is 0 (i.e., reached the end), scroll back to the first position
                        if (nextPosition == 3) {
                            binding.featuredRecyclerView.scrollToPosition(0)
                        }
                    }
                }
            }, delay, interval)
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
                timer.cancel()
                findNavController().navigate(R.id.fragmentCars)
            }
            else->{
                Toast.makeText(requireContext(),"This feature is under development",Toast.LENGTH_SHORT).show()
            }
        }
    }
}