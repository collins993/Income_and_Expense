package io.github.collins993.budgettracker2.registration

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import io.github.collins993.budgettracker2.MainActivity
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.ActivityLoginBinding
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.cardy.setBackgroundResource(R.drawable.bottom_radius)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
        }

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MyViewModel::class.java)

        binding.loginSignupBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.txtForgotPassowrd.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            if ( validateEmail() && validatePassword()){
                val email = binding.emailAddress.text.toString()
                val password = binding.password.text.toString()
                viewModel.signIn(email, password)

            }
            return@setOnClickListener
        }

        viewModel.signInStatus.observe(this, Observer { response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    if(response.data.equals("Login Successful",ignoreCase = true)){
                        Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Login failed with ${response.data}",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    val failedMessage =  response.message ?: "Unknown Error"
                    Toast.makeText(this,"Login failed with $failedMessage",Toast.LENGTH_LONG).show()
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

    /**
     * 1) field must not be empty
     * 2) password lenght must not be less than 6
     * 3) password must contain at least one digit
     * 4) password must contain atleast one upper and one lower case letter
     * 5) password must contain atleast one special character.
     */
    private fun validatePassword(): Boolean {
        if (binding.password.text.toString().trim().isEmpty()) {
            binding.txtInputLayoutPassword.error = "Required Field!"
            binding.password.requestFocus()
            return false
        } else if (binding.password.text.toString().length < 8) {
            binding.txtInputLayoutPassword.error = "password can't be less than 8"
            binding.password.requestFocus()
            return false
        } else {
            binding.txtInputLayoutPassword.isErrorEnabled = false
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