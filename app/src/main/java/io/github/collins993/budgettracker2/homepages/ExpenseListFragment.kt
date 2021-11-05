package io.github.collins993.budgettracker2.homepages

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.adapter.MyAdapter
import io.github.collins993.budgettracker2.databinding.BudgetListFragmentBinding
import io.github.collins993.budgettracker2.databinding.ExpenseListFragmentBinding
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory
import java.text.NumberFormat

class ExpenseListFragment : Fragment() {


    lateinit var recAdapter: MyAdapter
    private lateinit var viewModel: MyViewModel
    private var _binding: ExpenseListFragmentBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ExpenseListFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)


        setUpRecyclerView()

        viewModel.retrieveUserInfoFromFireStore()
        viewModel.retrieveIncomeItemFromFireStore()
        viewModel.retrieveExpenseItemFromFireStore()
        viewModel.retrieveRemainingBudgetAmount()

        viewModel.getUserStatus.observe(viewLifecycleOwner, {

            var country = it?.country.toString()
            binding.countryCode.text = country
            binding.countryCode2.text = country

        })

        viewModel.getExpenseItem.observe(viewLifecycleOwner, { documents ->

            recAdapter.differ.submitList(documents)

        })

        viewModel.totalBudgetStatus.observe(viewLifecycleOwner, { response ->

            response?.let {
                when(it){
                    is Resource.Success -> {

                        val format = NumberFormat.getIntegerInstance().format(it.data?.toInt())
                        binding.amount.text = format
                    }
                }
            }

        })
        viewModel.remainingBudgetStatus.observe(viewLifecycleOwner, { response ->
            response?.let {
                when(it){
                    is Resource.Success -> {
                        val format = NumberFormat.getIntegerInstance().format(it.data?.toInt())
                        binding.amount2.text = format
                    }
                }
            }

        })


        return root


    }

    private fun setUpRecyclerView() {
        recAdapter = MyAdapter()
        binding.expenseListRecyclerview.apply {
            adapter = recAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}