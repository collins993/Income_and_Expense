package io.github.collins993.budgettracker2.ui.statistics

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import io.github.collins993.budgettracker2.databinding.FragmentExpenseStatisticsBinding
import io.github.collins993.budgettracker2.databinding.FragmentStatisticsBinding
import io.github.collins993.budgettracker2.utils.Resource
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class ExpenseStatisticsFragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    private var _binding: FragmentExpenseStatisticsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpenseStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.retrieveTotalExpenseAmount()

        viewModel.totalExpenseAmountStatus.observe(viewLifecycleOwner, { response ->
            response?.let {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        val totalExpenseAmount = it.data

                        val pie = AnyChart.pie()
                        APIlib.getInstance().setActiveAnyChartView(binding.anyChartView2)
                        val data: MutableList<DataEntry> = ArrayList()
                        data.add(
                            ValueDataEntry(
                                "Food",
                                totalExpenseAmount?.totalFoodAmount
                            )
                        )
                        data.add(
                            ValueDataEntry(
                                "Social Life",
                                totalExpenseAmount?.totalSocialLifeAmount
                            )
                        )
                        data.add(
                            ValueDataEntry(
                                "Transportation",
                                totalExpenseAmount?.totalTransportationAmount
                            )
                        )
                        data.add(
                            ValueDataEntry(
                                "Household",
                                totalExpenseAmount?.totalHouseholdAmount
                            )
                        )
                        data.add(ValueDataEntry("Health", totalExpenseAmount?.totalHealthAmount))
                        data.add(ValueDataEntry("Gift", totalExpenseAmount?.totalGiftAmount))
                        data.add(
                            ValueDataEntry(
                                "Education",
                                totalExpenseAmount?.totalEducationAmount
                            )
                        )
                        data.add(ValueDataEntry("Other", totalExpenseAmount?.totalOtherAmount))

                        pie.data(data)

                        pie.title("Expense Statistics")
                        pie.labels().position("outside")
                        pie.legend().title().enabled(true)
                        pie.legend().title().text("Expense categories")
                        pie.legend().position("center-bottom")
                            .itemsLayout(LegendLayout.VERTICAL)
                            .align(Align.LEFT)
                        binding.anyChartView2.setChart(pie)
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