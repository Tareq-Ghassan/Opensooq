package com.opensooq.mobileApp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options
import com.squareup.picasso.Picasso

class ListStringIconAdapter(private val items: List<Options>) : RecyclerView.Adapter<ListStringIconAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.circle_image)
        private val textView: TextView = itemView.findViewById(R.id.circle_text)

        fun bind(item: Options) {
            if (item.optionImg.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
                textView.visibility = View.GONE
                Picasso.get()
                    .load(item.optionImg)
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_error)
                    .fit()
                    .centerCrop()
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE
                textView.visibility = View.VISIBLE
                textView.text = item.labelEn
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.circle_item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}