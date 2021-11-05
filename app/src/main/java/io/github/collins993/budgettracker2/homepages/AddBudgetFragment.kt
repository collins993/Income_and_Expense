package io.github.collins993.budgettracker2.homepages

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.AddBudgetFragmentBinding
import io.github.collins993.budgettracker2.models.Income
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddBudgetFragment : Fragment(R.layout.add_budget_fragment) {


    private lateinit var viewModel: MyViewModel
    val auth = FirebaseAuth.getInstance()

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

//        binding.incomeAmount.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                TODO("Not yet implemented")
//            }
//        })



        binding.saveIncomeBtn.setOnClickListener {

            val currentTime = Calendar.getInstance().time
            val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)
            Log.i("formattedDate", currentTime.toString())

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