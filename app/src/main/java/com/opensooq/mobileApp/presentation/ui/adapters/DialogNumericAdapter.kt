package com.opensooq.mobileApp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.data.models.Options

class DialogNumericAdapter (private val options: List<Options>, private val onClick: (Options) -> Unit) : RecyclerView.Adapter<DialogNumericAdapter.OptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return OptionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.bind(options[position], onClick)
    }

    override fun getItemCount(): Int = options.size

    class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(option: Options, onClick: (Options) -> Unit) {
            textView.text = option.labelEn
            itemView.setOnClickListener { onClick(option) }
        }
    }
}