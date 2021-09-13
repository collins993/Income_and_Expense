package io.github.collins993.budgettracker2.homepages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.AddExpensesFragmentBinding
import io.github.collins993.budgettracker2.viewmodel.MyViewModel

class AddExpensesFragment : Fragment(R.layout.add_expenses_fragment) {


    private lateinit var viewModel: MyViewModel

    private lateinit var binding: AddExpensesFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddExpensesFragmentBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        val expenses = resources.getStringArray(R.array.expenses_categories)
        val currency = resources.getStringArray(R.array.currency)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, expenses)
        binding.autoCompleteTxtCategory.setAdapter(arrayAdapter)





    }
    //viewModel = ViewModelProvider(this).get(AddExpensesViewModel::class.java)
}