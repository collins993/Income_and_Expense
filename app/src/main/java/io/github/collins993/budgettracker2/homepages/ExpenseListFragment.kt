package io.github.collins993.budgettracker2.homepages

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.viewmodel.MyViewModel

class ExpenseListFragment : Fragment() {

    companion object {
        fun newInstance() = ExpenseListFragment()
    }

    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.expense_list_fragment, container, false)
    }


}