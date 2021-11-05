package io.github.collins993.budgettracker2.profilepages

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.FragmentResetPasswordBinding
import io.github.collins993.budgettracker2.registration.LoginActivity
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory


class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var viewModel: MyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetPasswordBinding.bind(view)
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(MyViewModel::class.java)

        binding.resetPasswordBtn.setOnClickListener {
            if ( validateEmail()){
                val email = binding.emailAddress.text.toString()
                viewModel.resetPassword(email)
                   findNavController().navigate(R.id.action_resetPasswordFragment_to_profileSettingsFragment)
                Toast.makeText(requireActivity(),"Reset Successful", Toast.LENGTH_LONG).show()
            }
            return@setOnClickListener
        }

        viewModel.resetPasswordStatus.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    if(response.data.equals("Reset Successful",ignoreCase = true)){
                        Toast.makeText(requireActivity(),"Reset Successful", Toast.LENGTH_LONG).show()

//                        requireActivity().finish()
                    }
                    else{
                        Toast.makeText(requireActivity(),"Reset failed with ${response.data}", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    val failedMessage =  response.message ?: "Unknown Error"
                    Toast.makeText(requireActivity(),"Reset failed with $failedMessage", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

    }

    private fun validateEmail(): Boolean {
        if (binding.emailAddress.text.toString().trim().isEmpty()) {
            binding.txtInputLayoutEmail.error = "Required Field!"
            binding.emailAddress.requestFocus()
            return false
        }  else {
            binding.txtInputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun hideProgressBar() {
        binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressCircular.visibility = View.VISIBLE
    }
}