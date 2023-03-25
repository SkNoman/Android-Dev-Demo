package com.example.crud.ui.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentLoginBinding


class FragmentLogin: BaseFragmentWithBinding<FragmentLoginBinding>(FragmentLoginBinding::inflate)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentUserDashboard)
        }

    }
}