package com.opensooq.mobileApp.presentation.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * `DefaultViewHolder` is a basic ViewHolder used to display a default or empty state
 * in a RecyclerView when no specific data needs to be bound.
 *
 * @param itemView The view associated with this ViewHolder.
 */
class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Binds the view to the data. In this case, no specific data is bound, so this
     * method can either be left empty or used to bind default content if needed.
     */
    fun bind() {
        // Optionally bind some default content or leave empty
    }
}