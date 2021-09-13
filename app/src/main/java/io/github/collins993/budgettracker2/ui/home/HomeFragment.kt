package io.github.collins993.budgettracker2.ui.home

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.UserManager
import io.github.collins993.budgettracker2.databinding.FragmentHomeBinding
import io.github.collins993.budgettracker2.models.User
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var userManager: UserManager
    var country = ""
    var name = ""
    var email = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(MyViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userManager = UserManager(requireActivity())
        binding.budgetCard.setBackgroundResource(R.drawable.card_gradient_background)
        binding.expenseCard.setBackgroundResource(R.drawable.expense_card_gradient_background)


        binding.fabAddBudget.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addBudgetFragment)
        }
        binding.fabAddExpense.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addExpensesFragment)
        }
        binding.btnSeeAllBudgetItem.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_home_to_budgetListFragment)
        }
        binding.btnSeeAllExpenseItem.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_home_to_expenseListFragment)
        }

        viewModel.retrieveUserFromFireBaseDatabase()
        viewModel.getUserDetails.observe(viewLifecycleOwner, { snapShot ->
            when(snapShot){
                is Resource.Success ->{
                    snapShot.data?.let { dataSnapshot ->
                        val value = dataSnapshot.getValue(User::class.java)
                        binding.welcomeTxt.text = "Welcome ${value?.username} \uD83D\uDE00"
                        binding.amount.text = "${value?.country}  50,000"
                        binding.expensesAmount.text = "${value?.country}  27,785"
                    }
                }
                is Resource.Error -> { }
                is Resource.Loading -> { }
            }
        })

//        userManager.usernameFlow.asLiveData().observe(viewLifecycleOwner, {
//            name = it
//            binding.welcomeTxt.text = "Welcome $it \uD83D\uDE00"
//        })
//        userManager.emailFlow.asLiveData().observe(viewLifecycleOwner, {
//            email = it
//        })
//        userManager.countryFlow.asLiveData().observe(viewLifecycleOwner, {
//            country = it
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}