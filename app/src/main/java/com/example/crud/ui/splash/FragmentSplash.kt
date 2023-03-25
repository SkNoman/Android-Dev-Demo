package com.example.crud.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.crud.BuildConfig
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentSplashBinding
import com.example.crud.utils.Constant.BASE_URL_END_POINT
import com.example.crud.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class FragmentSplash : BaseFragmentWithBinding<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(IO).launch {
            getBaseUrl()
        }
    }

    private suspend fun getBaseUrl() {
        withContext(Main){
            try {
                splashViewModel.getBaseUrl(BuildConfig.BASE_URL+BASE_URL_END_POINT)

                splashViewModel.baseUrlLiveData.observe(viewLifecycleOwner){
                    if (it.result_code == 0){
                        Log.e("nlog_thread_fragment", Thread.currentThread().toString())
                        Log.e("nlog",it.result.base_url)
                    }else{
                        Log.e("nlog","not found")
                    }
                }
                splashViewModel.baseUrlErrorResponse.observe(viewLifecycleOwner){ it ->
                    it.getContentIfNotHandled()?.let {
                        Log.e("nlog",it.message)
                    }
                }
            }catch (e:Exception){
                Log.e("error",e.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this).asGif().load(R.drawable.loader_slow_jump_moving).into(binding.loader)
        binding.loader.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSplash_to_fragmentLogin)
        }
    }

}