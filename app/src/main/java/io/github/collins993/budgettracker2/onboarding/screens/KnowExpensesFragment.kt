package io.github.collins993.budgettracker2.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.FragmentKnowExpensesBinding

class KnowExpensesFragment : Fragment(R.layout.fragment_know_expenses) {

    private lateinit var binding: FragmentKnowExpensesBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentKnowExpensesBinding.bind(view)


    }

}