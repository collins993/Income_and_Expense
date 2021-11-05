package io.github.collins993.budgettracker2.ui.statistics

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.anychart.APIlib
import com.anychart.chart.common.dataentry.ValueDataEntry

import com.anychart.chart.common.dataentry.DataEntry

import com.anychart.AnyChart
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import io.github.collins993.budgettracker2.R
import io.github.collins993.budgettracker2.databinding.FragmentStatisticsBinding

import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory


class StatisticsFragment : Fragment() {
    private lateinit var viewModel: MyViewModel
    private var _binding: FragmentStatisticsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        binding.txtExpense.setOnClickListener {
            findNavController().navigate(R.id.action_statisticsFragment2_to_expenseStatisticsFragment)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.retrieveTotalIncomeAmount()

        viewModel.totalIncomeAmountStatus.observe(viewLifecycleOwner, { response ->
            response?.let {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        Log.i("Total Allowance", it.data.toString())

                        val totalIncomeAmount = it.data

                        val pie = AnyChart.pie()
                        APIlib.getInstance().setActiveAnyChartView(binding.anyChartView)
                        val data: MutableList<DataEntry> = ArrayList()
                        data.add(
                            ValueDataEntry(
                                "Allowance",
                                totalIncomeAmount?.totalAllowanceAmount
                            )
                        )
                        data.add(ValueDataEntry("Salary", totalIncomeAmount?.totalSalaryAmount))
                        data.add(
                            ValueDataEntry(
                                "Petty cash",
                                totalIncomeAmount?.totalPettyCashAmount
                            )
                        )
                        data.add(ValueDataEntry("Bonus", totalIncomeAmount?.totalBonusAmount))
                        data.add(ValueDataEntry("Other", totalIncomeAmount?.totalOtherAmount))

                        pie.data(data)

                        pie.title("Income Statistics")
                        pie.labels().position("outside")
                        pie.legend().title().enabled(true)
                        pie.legend().title().text("Income categories")
                        pie.legend().position("center-bottom")
                            .itemsLayout(LegendLayout.VERTICAL)
                            .align(Align.LEFT)
                        binding.anyChartView.setChart(pie)
                    }
                    is Resource.Error -> {
                        Log.e("Error", it.data.toString() )
                        hideProgressBar()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }

        })




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun hideProgressBar() {
        binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressCircular.visibility = View.VISIBLE
    }
}