package io.github.collins993.budgettracker2.registration

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import io.github.collins993.budgettracker2.MainActivity
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.UserManager
import io.github.collins993.budgettracker2.databinding.ActivitySignUpBinding
import io.github.collins993.budgettracker2.models.User
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var userManager: UserManager

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.cardy.setBackgroundResource(R.drawable.top_radius)

//        val auth = FirebaseAuth.getInstance()
//        if (auth.currentUser != null){
//            startActivity(Intent(this, MainActivity::class.java))
//        }

        userManager = UserManager(this)
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MyViewModel::class.java)

        binding.signupLoginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerBtn.setOnClickListener {
            if (validateUserName() && validateEmail() && validatePassword()){
                val username = binding.username.text.toString().trim()
                val email = binding.emailAddress.text.toString().trim()
                val password = binding.password.text.toString().trim()
                val country = binding.autoCompleteTxt.text.toString()

                viewModel.signUp(email,password)


//                GlobalScope.launch {
//                    userManager.storeUser(username, email, country)
//                }
            }
            return@setOnClickListener

        }



        observeRegistration()
        viewModel.addUserStatus.observe(this, {
            when(it){
                is Resource.Success -> {
                    hideProgressBar()
                    if(it.data.equals("Successfully added user",ignoreCase = true)){
                        Toast.makeText(this,"Successfully added user",Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this,"Failed to add  user with ${it.data}",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    val failedMessage =  it.message ?: "Unknown Error"
                    Toast.makeText(this,"Registration failed with $failedMessage",Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        val currency = resources.getStringArray(R.array.currency)
        val arrayAdapter2 = ArrayAdapter(this, R.layout.dropdown_item, currency)
        binding.autoCompleteTxt.setAdapter(arrayAdapter2)

    }

    private fun observeRegistration(){
        viewModel.registrationStatus.observe(this, Observer {result->
            result?.let {
                when(it){
                    is Resource.Success ->{
                        hideProgressBar()
                        if(it.data.equals("UserCreated",ignoreCase = true)){
                            Toast.makeText(this,"Registration Successful User created",Toast.LENGTH_SHORT).show()
                            val username = binding.username.text.toString().trim()
                            val email = binding.emailAddress.text.toString().trim()
                            val country = binding.autoCompleteTxt.text.toString()
                            var user = User(username,email,country)
                            viewModel.storeUserInFireBaseDatabase(user)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"Registration failed with ${it.data}",Toast.LENGTH_LONG).show()
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        val failedMessage =  it.message ?: "Unknown Error"
                        Toast.makeText(this,"Registration failed with $failedMessage",Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }

        })
    }

    private fun hideProgressBar() {
        binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressCircular.visibility = View.VISIBLE
    }
    /**
     * field must not be empy
     */
    private fun validateUserName(): Boolean {
        if (binding.username.text.toString().trim().isEmpty()) {
            binding.txtInputLayoutUsername.error = "Required Field!"
            binding.username.requestFocus()
            return false
        } else {
            binding.txtInputLayoutUsername.isErrorEnabled = false
        }
        return true
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
}