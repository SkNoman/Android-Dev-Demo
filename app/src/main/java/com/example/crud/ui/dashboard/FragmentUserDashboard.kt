package com.example.crud.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager2.widget.ViewPager2
import com.example.crud.BuildConfig
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentUserDashboardBinding
import com.example.crud.model.SlideItem
import com.example.crud.model.dashboard.FeaturedItem
import com.example.crud.model.dashboard.MenusItem
import com.example.crud.network.APIEndpoint
import com.example.crud.ui.adapters.DashboardMainMenuAdapter
import com.example.crud.ui.adapters.FeaturedListItemAdapter
import com.example.crud.ui.adapters.OnClickMenu
import com.example.crud.ui.adapters.SlideItemAdapter
import com.example.crud.utils.CheckNetwork
import com.example.crud.utils.GoogleMaps
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
    (FragmentUserDashboardBinding:: inflate),OnClickMenu,OnRefreshListener {
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val demoViewModel: DemoViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var swipeLayout: SwipeRefreshLayout


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(viewPagerHotItemRunnable)
        handler.removeCallbacks(viewPagerRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(viewPagerHotItemRunnable, 2000)
        handler.postDelayed(viewPagerRunnable,3000)
    }
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
            autoSliderHotItem()
            featuredRecycler()
            slideImageItem()
        }

        swipeLayout = binding.layoutDashboard
        swipeLayout.setOnRefreshListener(this)

        binding.txtTrendingNow.setOnClickListener{
            GoogleMaps.openGoogleMaps(requireActivity(),23.9756079,90.394622)
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

    val viewPagerRunnable = object : Runnable {
        override fun run() {
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
            handler.postDelayed(this, 2000)
        }
    }
    val viewPagerHotItemRunnable = object : Runnable {
        override fun run() {
            binding.viewPagerHotItem.currentItem = binding.viewPager.currentItem + 1
            handler.postDelayed(this, 2000)
        }
    }



    private fun autoSliderHotItem() {
        val sliderItem :ArrayList<SlideItem> = ArrayList()
        sliderItem.add(SlideItem(R.drawable.item3))
        sliderItem.add(SlideItem(R.drawable.item2))
        sliderItem.add(SlideItem(R.drawable.item1))
        binding.viewPagerHotItem.apply {
            adapter = SlideItemAdapter(sliderItem)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = sliderItem.size
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        /*   val compositionTransformer = CompositePageTransformer()
           compositionTransformer.addTransformer(MarginPageTransformer(10))
           compositionTransformer.addTransformer { page, position ->
               val r = 1 - kotlin.math.abs(position)
               page.scaleY = 0.80f + r * 01.1f
           }
           binding.viewPager.setPageTransformer(compositionTransformer)*/

        binding.viewPagerHotItem.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(viewPagerHotItemRunnable)
                handler.postDelayed(viewPagerHotItemRunnable, 2000)
            }
        })
    }

    private fun slideImageItem() {
        val sliderItem :ArrayList<SlideItem> = ArrayList()
        sliderItem.add(SlideItem(R.drawable.item1))
        sliderItem.add(SlideItem(R.drawable.item2))
        sliderItem.add(SlideItem(R.drawable.item3))
        binding.viewPager.apply {
            adapter = SlideItemAdapter(sliderItem)
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = sliderItem.size
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
     /*   val compositionTransformer = CompositePageTransformer()
        compositionTransformer.addTransformer(MarginPageTransformer(10))
        compositionTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.80f + r * 01.1f
        }
        binding.viewPager.setPageTransformer(compositionTransformer)*/

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(viewPagerRunnable)
                handler.postDelayed(viewPagerRunnable, 3000)
            }
        })



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
            }else{
                Toast(requireContext()).showCustomToast("No menu found",requireActivity())
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
            2->{
                findNavController().navigate(R.id.fragmentFridge)
            }
            else->{
                Toast.makeText(requireContext(),getString(R.string.this_feature_is_under_development),Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRefresh() {
        swipeLayout.isRefreshing = false
    }
}