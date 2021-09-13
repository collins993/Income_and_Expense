package io.github.collins993.budgettracker2.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.collins993.budgettracker2.databinding.AdapterLayoutBinding
import io.github.collins993.budgettracker2.models.Income
import io.github.collins993.budgettracker2.viewmodel.MyViewModel
import io.github.collins993.budgettracker2.viewmodel.MyViewModelFactory

class MyAdapter(): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private lateinit var viewModel: MyViewModel

    inner class ViewHolder(val binding: AdapterLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Income>() {

        override fun areItemsTheSame(oldItem: Income, newItem: Income): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Income, newItem: Income): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val income = differ.currentList[position]
        holder.binding.dateAndTimeTxt.text = income.date
        holder.binding.categoryTxt.text = income.category
        holder.binding.amountTxt.text = income.amount
        holder.binding.noteTxt.text = income.note
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Income) -> Unit)? = null


    fun setOnItemClickListener(listener: (Income) -> Unit){
        onItemClickListener = listener
    }
}