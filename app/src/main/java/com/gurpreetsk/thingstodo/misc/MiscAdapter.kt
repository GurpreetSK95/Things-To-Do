package com.gurpreetsk.thingstodo.misc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import books.Book
import com.gurpreetsk.thingstodo.R
import kotlinx.android.synthetic.main.list_item_book.view.bookAuthorTextView
import kotlinx.android.synthetic.main.list_item_book.view.bookNameTextView
import kotlinx.android.synthetic.main.list_item_misc.view.miscNameTextView
import kotlinx.android.synthetic.main.list_item_misc.view.miscNotesTextView
import misc.Misc

class MiscAdapter : ListAdapter<Misc, MiscViewHolder>(MiscDiffUtilCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiscViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_misc, parent, false)

    return MiscViewHolder(view)
  }

  override fun onBindViewHolder(holder: MiscViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

class MiscViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(misc: Misc) {
    view.miscNameTextView.text = misc.name
    view.miscNotesTextView.text = misc.notes
  }
}

private class MiscDiffUtilCallback : DiffUtil.ItemCallback<Misc>() {
  override fun areItemsTheSame(oldItem: Misc, newItem: Misc): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Misc, newItem: Misc): Boolean =
      oldItem.name == newItem.name && oldItem.notes == newItem.notes
}
