package com.example.crud.ui.dashboard.menus

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.crud.BuildConfig
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentCarsBinding
import com.example.crud.model.menu.CarListItem
import com.example.crud.network.APIEndpoint
import com.example.crud.ui.adapters.CarListAdapter
import com.example.crud.ui.adapters.OnClickCar
import com.example.crud.utils.CheckNetwork
import com.example.crud.utils.Constant
import com.example.crud.utils.showCustomToast
import com.example.crud.viewmodel.menus.CarsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCars : BaseFragmentWithBinding<FragmentCarsBinding>
    (FragmentCarsBinding::inflate),OnClickCar
{
    private val carsViewModel:CarsViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       if(CheckNetwork(requireContext()).isNetworkConnected){
           carsViewModel.getAllCarList(BuildConfig.BASE_URL+APIEndpoint.ALL_CARS_LIST)
       }else{
           Toast(requireContext()).showCustomToast(getString(R.string.pls_chk_internet),
               requireActivity())
       }

        observeCarList()

        Glide.with(requireActivity()).
        load("https://wallpaperaccess.com/full/1628631.jpg")
            .placeholder(R.drawable.preloader).into(binding.ivCarHeading)


    }

    private fun observeCarList() {
        carsViewModel.allCarList.observe(viewLifecycleOwner) {
            if (it.carlist.isNullOrEmpty()){
                Toast(requireContext()).showCustomToast(Constant.ERROR_MESSAGE,requireActivity())
            }else{
                showCarList(it.carlist)
            }

        }
        carsViewModel.errorResponse.observe(viewLifecycleOwner){
            if (it.status == 500){
                Toast(requireContext()).showCustomToast(it.message,requireActivity())
                findNavController().popBackStack()
            }
        }
    }

    private fun showCarList(list: List<CarListItem?>) {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = CarListAdapter(requireContext(),list,this)
    }

    override fun onClick(id: Int) {
        Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
    }
}