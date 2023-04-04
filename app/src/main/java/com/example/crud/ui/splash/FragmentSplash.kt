package com.example.crud.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSplash : BaseFragmentWithBinding<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).asGif().load(R.drawable.loader_slow_jump_moving).into(binding.loader)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.fragmentUserDashboard)
        }, 3000)
    }
}