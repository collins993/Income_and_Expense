package io.github.collins993.budgettracker2.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWelcomeBinding.bind(view)

        val viewPager2 = activity?.findViewById<ViewPager2>(R.id.view_pager_2)

        binding.buttonNext.setOnClickListener {
            viewPager2?.currentItem = 1
        }
    }
}