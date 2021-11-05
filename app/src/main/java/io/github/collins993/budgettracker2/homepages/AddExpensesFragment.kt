package io.github.collins993.budgettracker2.homepages

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.AddExpensesFragmentBinding
import io.github.collins993.budgettracker2.models.Income
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddExpensesFragment : Fragment(R.layout.add_expenses_fragment) {


    private lateinit var viewModel: MyViewModel
    val auth = FirebaseAuth.getInstance()

    private lateinit var binding: AddExpensesFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddExpensesFragmentBinding.bind(view)
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(MyViewModel::class.java)

        getCurrentDateTime()

        binding.saveIncomeBtn.setOnClickListener {

            val currentTime = Calendar.getInstance().time

            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val sdf = SimpleDateFormat("MM-dd-yyyy")
            val formatedDate = sdf.format(date)

            val dateAndTime = formatedDate
            val account = binding.autoCompleteTxtAccount.text.toString()
            val category = binding.autoCompleteTxtCategory.text.toString()
            val amount = binding.incomeAmount.text.toString()
            val note = binding.incomeNote.text.toString()
            val uid = auth.currentUser?.uid.toString()

            val income = Income(dateAndTime,amount,category,account,note,uid)
            viewModel.addExpenseItemToFireStore(income)
            Toast.makeText(requireActivity(), "Income Item Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addExpensesFragment_to_navigation_home)

        }
    }

    override fun onResume() {
        super.onResume()
        val expenses = resources.getStringArray(R.array.expenses_categories)
        val accounts = resources.getStringArray(R.array.accounts)
        val expenseArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, expenses)
        val accountArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, accounts)
        binding.autoCompleteTxtCategory.setAdapter(expenseArrayAdapter)
        binding.autoCompleteTxtAccount.setAdapter(accountArrayAdapter)





    }

    private fun getCurrentDateTime() {
        val currentTime = Calendar.getInstance().time
        val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

        Log.i("CurrentDate", formattedDate)
        Log.i("CurrentTime", currentTime.toString())
    }
    //viewModel = ViewModelProvider(this).get(AddExpensesViewModel::class.java)
}