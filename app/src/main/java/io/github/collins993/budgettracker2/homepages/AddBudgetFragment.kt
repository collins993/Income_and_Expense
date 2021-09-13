package io.github.collins993.budgettracker2.homepages

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.AddBudgetFragmentBinding
import io.github.collins993.budgettracker2.models.Income
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory


class AddBudgetFragment : Fragment(R.layout.add_budget_fragment) {


    private lateinit var viewModel: MyViewModel

    private lateinit var binding: AddBudgetFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddBudgetFragmentBinding.bind(view)

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(MyViewModel::class.java)


//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            // Handle the back button event
//            findNavController().navigate(R.id.action_addBudgetFragment_to_navigation_home)
//        }



        binding.saveIncomeBtn.setOnClickListener {
            val dateAndTime = binding.incomeDateAndTime.text.toString()
            val account = binding.autoCompleteTxtAccount.text.toString()
            val category = binding.autoCompleteTxtCategory.text.toString()
            val amount = binding.incomeAmount.text.toString()
            val note = binding.incomeNote.text.toString()

            val income = Income(dateAndTime,amount,category,account,note)
            viewModel.addIncomeItemToFireStore(income)
            Toast.makeText(requireActivity(), "Income Item Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addBudgetFragment_to_navigation_home)

        }
//
//        viewModel.addIncomeStatus.observe(viewLifecycleOwner, { response ->
//            when(response){
//                is Resource.Success -> {
//                    if (response.data?.equals("Income Item Saved") == true){
//
//                    }
//                }
//                is Resource.Error -> {
//                    Toast.makeText(requireActivity(), "Failed to save item", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })

    }


    override fun onResume() {
        super.onResume()
        val income = resources.getStringArray(R.array.income_categories)
        val accounts = resources.getStringArray(R.array.accounts)
        val incomeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, income)
        val accountArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, accounts)
        binding.autoCompleteTxtCategory.setAdapter(incomeArrayAdapter)
        binding.autoCompleteTxtAccount.setAdapter(accountArrayAdapter)





    }
//    viewModel = ViewModelProvider(this).get(AddBudgetViewModel::class.java)
}