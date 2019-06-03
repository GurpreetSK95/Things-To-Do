package com.gurpreetsk.thingstodo.books

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

class BooksAdapter : ListAdapter<Book, BookViewHolder>(BookDiffUtilCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_book, parent, false)

    return BookViewHolder(view)
  }

  override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

class BookViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(book: Book) {
    view.bookNameTextView.text = book.name
    view.bookAuthorTextView.text = book.author
    view.setBackgroundColor(ContextCompat.getColor(view.context, if (book.isRead == 1L) {
      R.color.colorConsumedContent
    } else {
      R.color.transparent
    }))
  }
}

private class BookDiffUtilCallback : DiffUtil.ItemCallback<Book>() {
  override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
      oldItem.name == newItem.name && oldItem.author == newItem.author && oldItem.isRead == newItem.isRead
}
