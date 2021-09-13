package io.github.collins993.budgettracker2.registration

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.collins993.budgettracker2.MainActivity
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.ActivityLoginBinding
import io.github.collins993.budgettracker2.databinding.ActivityResetPasswordBinding
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MyViewModel::class.java)



        binding.resetPasswordBtn.setOnClickListener {
            if ( validateEmail()){
                val email = binding.emailAddress.text.toString()
                viewModel.resetPassword(email)
                finish()
                Toast.makeText(this,"Reset Successful", Toast.LENGTH_LONG).show()
            }
            return@setOnClickListener
        }

        viewModel.resetPasswordStatus.observe(this, Observer { response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    if(response.data.equals("Reset Successful",ignoreCase = true)){
                        Toast.makeText(this,"Reset Successful", Toast.LENGTH_LONG).show()

                    }
                    else{
                        Toast.makeText(this,"Reset failed with ${response.data}", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    val failedMessage =  response.message ?: "Unknown Error"
                    Toast.makeText(this,"Reset failed with $failedMessage", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }



    /**
     * 1) field must not be empty
     * 2) text should matches email address format
     */
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