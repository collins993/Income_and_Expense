package io.github.collins993.budgettracker2.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2
import io.github.collins993.budgettracker2.UserManager
import io.github.collins993.budgettracker2.databinding.ActivityViewPagerBinding
import io.github.collins993.budgettracker2.onboarding.screens.KnowExpensesFragment
import io.github.collins993.budgettracker2.onboarding.screens.TrackBudgetFragment
import io.github.collins993.budgettracker2.onboarding.screens.WelcomeFragment
import io.github.collins993.budgettracker2.registration.LoginActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding
    private lateinit var userManager: UserManager
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userManager = UserManager(this)

        val fragmentsList = arrayListOf<Fragment>(
            WelcomeFragment(),
            TrackBudgetFragment(),
            KnowExpensesFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentsList,
            supportFragmentManager,
            lifecycle
        )

        binding.viewPager2.adapter = adapter


        var doppelgangerPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //Toast.makeText(applicationContext, "Selected position: ${position}",Toast.LENGTH_SHORT).show()
                if (position == 2){
                    binding.button.visibility = View.VISIBLE
                }
                else{
                    binding.button.visibility = View.GONE
                }
            }
        }

        binding.viewPager2.registerOnPageChangeCallback(doppelgangerPageChangeCallback)

        binding.button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            GlobalScope.launch {
                userManager.onboardingFinished(true)
            }
            finish()
        }

    }




}