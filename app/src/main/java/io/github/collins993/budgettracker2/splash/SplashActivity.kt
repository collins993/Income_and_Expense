package io.github.collins993.budgettracker2.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.asLiveData
import io.github.collins993.budgettracker2.MainActivity
import io.github.collins993.budgettracker2.UserManager
import io.github.collins993.budgettracker2.databinding.ActivitySplashBinding
import io.github.collins993.budgettracker2.onboarding.ViewPagerActivity
import io.github.collins993.budgettracker2.registration.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userManager = UserManager(this)

        Handler().postDelayed({
            onBoardingFinished()
            finish()
        }, 3000)


    }
    private fun onBoardingFinished() {
        userManager.isFinishedFlow.asLiveData().observe(this, {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else{
                startActivity(Intent(this, ViewPagerActivity::class.java))
            }
        })
    }
}