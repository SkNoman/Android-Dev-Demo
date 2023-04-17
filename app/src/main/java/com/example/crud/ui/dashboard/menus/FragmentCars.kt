package com.example.crud.ui.dashboard.menus

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentCarsBinding
import com.example.crud.model.dashboard.MenusItem
import com.example.crud.ui.adapters.CarListAdapter
import com.example.crud.ui.adapters.OnClickCar
import com.example.crud.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCars : BaseFragmentWithBinding<FragmentCarsBinding>
    (FragmentCarsBinding::inflate),OnClickCar
{
    private val dashboardViewModel: DashboardViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel.getDashboardMainMenuFromLocalDB.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                showCarList(it)
            }
        }


    }

    private fun showCarList(list: List<MenusItem>) {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = CarListAdapter(requireContext(),list,this)
    }

    override fun onClick(id: Int) {
        Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
    }
}