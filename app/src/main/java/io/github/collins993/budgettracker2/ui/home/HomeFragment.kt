package io.github.collins993.budgettracker2.ui.home

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.UserManager
import io.github.collins993.budgettracker2.databinding.FragmentHomeBinding
import io.github.collins993.budgettracker2.models.User
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory
import java.text.NumberFormat

class HomeFragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var userManager: UserManager
    private lateinit var country: String
    private lateinit var email: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userManager = UserManager(requireActivity())
        binding.budgetCard.setBackgroundResource(R.drawable.card_gradient_background)
        binding.expenseCard.setBackgroundResource(R.drawable.expense_card_gradient_background)
        //viewModel.addUserInfoToFireStore(User("locks", "locks@mail.com", "BRA", "myUid"))
        viewModel.retrieveUserInfoFromFireStore()
        viewModel.retrieveIncomeItemFromFireStore()
        viewModel.retrieveExpenseItemFromFireStore()

        viewModel.getUserStatus.observe(viewLifecycleOwner, {
            Log.i("UserInfo", it?.username.toString())
            binding.welcomeTxt.text = "Welcome ${it?.username} \uD83D\uDE00"
            country = it?.country.toString()
            email = it?.emailAddress.toString()
            binding.countryCode.text = "$country"
            binding.countryCodeExpense.text = "$country"

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

        viewModel.totalExpenseStatus.observe(viewLifecycleOwner, { response ->
            response?.let {
                when(it){
                    is Resource.Success -> {
                        val format = NumberFormat.getIntegerInstance().format(it.data?.toInt())
                        binding.expenseAmount.text = format
                    }
                }
            }

        })




        binding.fabAddBudget.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addBudgetFragment)
        }
        binding.fabAddExpense.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addExpensesFragment)
        }
        binding.btnSeeAllBudgetItem.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_budgetListFragment)
        }
        binding.btnSeeAllExpenseItem.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_expenseListFragment)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}