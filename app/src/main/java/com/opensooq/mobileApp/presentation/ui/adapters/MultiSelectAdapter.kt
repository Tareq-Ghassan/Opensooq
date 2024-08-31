package com.opensooq.mobileApp.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.data.models.Options

class MultiSelectAdapter(private var options: List<Options>) : RecyclerView.Adapter<MultiSelectAdapter.OptionViewHolder>() {

    private val selectedOptions = mutableSetOf<Options>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option, selectedOptions.contains(option))
    }

    override fun getItemCount(): Int = options.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newOptions: List<Options>) {
        options = newOptions
        notifyDataSetChanged()
    }

    fun getSelectedOptions(): List<Options> = selectedOptions.toList()

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox = itemView as CheckedTextView

        @SuppressLint("NotifyDataSetChanged")
        fun bind(option: Options, isSelected: Boolean) {
            checkBox.text = option.labelEn
            checkBox.isChecked = isSelected

            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    selectedOptions.add(option)
                } else {
                    selectedOptions.remove(option)
                }
                notifyDataSetChanged()
            }
        }
    }
}