package com.opensooq.mobileApp.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options

class MultiSelectAdapter(
    private var options: List<Options>,
    private val selectedOptions: MutableList<Options>
) : RecyclerView.Adapter<MultiSelectAdapter.MultiSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_select_item, parent, false)
        return MultiSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option, selectedOptions.contains(option))
    }

    override fun getItemCount(): Int = options.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(filteredOptions: List<Options>) {
        options = filteredOptions
        notifyDataSetChanged()
    }

    fun getSelectedOptions(): List<Options> = selectedOptions

    inner class MultiSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_view)
        private val checkBox: CheckBox = itemView.findViewById(R.id.check_box)

        fun bind(option: Options, isSelected: Boolean) {
            textView.text = option.labelEn
            checkBox.isChecked = isSelected

            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                if (checkBox.isChecked) {
                    selectedOptions.add(option)
                } else {
                    selectedOptions.remove(option)
                }
            }
        }
    }
}
