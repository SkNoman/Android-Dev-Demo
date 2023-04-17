package com.example.crud.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.crud.BuildConfig.BASE_URL
import com.example.crud.R
import com.example.crud.base.BaseFragmentWithBinding
import com.example.crud.databinding.FragmentLoginBinding
import com.example.crud.network.APIEndpoint.LOGIN
import com.example.crud.utils.Constant
import com.example.crud.utils.showCustomToast
import com.example.crud.viewmodel.SignInSignUpViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLogin: BaseFragmentWithBinding<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val signInSignUpViewModel: SignInSignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {

            val validationMessage = validateInputs()

            if (validationMessage == "ok"){

                val body = JsonObject()
                body.addProperty("phone",binding.etUserName.text.toString())
                body.addProperty("password",binding.etUserPassword.text.toString())
                try {
                    signInSignUpViewModel.signIn(BASE_URL+ LOGIN,body)
                }catch (e:Exception){
                    Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_LONG).show()
                }

            }else{
                Toast(requireContext()).showCustomToast(validationMessage,requireActivity())
            }
        }

        //OBSERVE LOGIN RESPONSE
        signInSignUpViewModel.loginResponseLiveData.observe(viewLifecycleOwner) {
            if (it.status == 200) {
                if (it.isSuccess == true) {
                    binding.etUserPassword.text = null
                    findNavController().navigate(R.id.fragmentUserDashboard)
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                } else if (it.isSuccess == false) {
                    showToast(it.message.toString())
                }
            }
        }

        //OBSERVE LOGIN ERROR RESPONSE
        signInSignUpViewModel.errorResponse.observe(viewLifecycleOwner) {
            if (it.status == 500) {
                showToast(it.message)
            } else {
                showToast(Constant.ERROR_MESSAGE)
            }
        }
    }


    private fun validateInputs(): String {
        binding.apply {
            if (etUserName.text.toString().isEmpty()){
                return "Phone no required!"
            }else if (etUserName.text.toString().length<11){
                return "Phone no minimum length is 11"
            }
            else if (etUserPassword.text.toString().isEmpty()){
                return "Password required!"
            }else if (etUserPassword.text.toString().length <6){
                return "Password minimum length is 6"
            }
        }
        return "ok"
    }

    private fun showToast(message: String){
        Toast(requireContext()).showCustomToast(message,requireActivity())
    }
}