package com.example.crud.ui.dashboard.menus

import android.os.Bundle
import android.view.View
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentCarsBinding


class FragmentCars : BaseFragmentWithBinding<FragmentCarsBinding>
    (FragmentCarsBinding::inflate)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // binding.txtDemo.text = "Welcome To Cars Fragment"
    }
}