package com.gurpreetsk.thingstodo.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gurpreetsk.thingstodo.Injection
import com.gurpreetsk.thingstodo.R
import com.squareup.sqldelight.Query
import kotlinx.android.synthetic.main.books_fragment.addBookButton
import kotlinx.android.synthetic.main.books_fragment.booksRecyclerView
import java.util.Date

const val TAG_BOOKS_FRAGMENT = "TAG_BOOKS_FRAGMENT"

class BooksFragment : Fragment() {
  companion object {
    fun newInstance(): BooksFragment {
      return BooksFragment()
    }
  }

  private val database by lazy { Injection.getDatabase(activity!!) }

  private val queryListener by lazy {
    object : Query.Listener {
      override fun queryResultsChanged() {
        showMoviesList()
      }
    }
  }

  private val addBookDialog by lazy {
    val dialogView = layoutInflater.inflate(R.layout.layout_book_input_dialog, null)
    val bookNameEditText = dialogView.findViewById(R.id.bookNameEditText) as EditText
    val bookAuthorEditText = dialogView.findViewById(R.id.bookAuthorEditText) as EditText

    AlertDialog.Builder(requireContext())
        .setTitle(R.string.add_book)
        .setCancelable(false)
        .setView(dialogView)
        .setPositiveButton(R.string.add) { dialog, which ->
          database.booksQueries
              .insertOne(bookNameEditText.text.toString(), bookAuthorEditText.text.toString(), 0L, Date().time)
              .also {
                dialog.dismiss()
              }
        }
        .setNegativeButton(R.string.cancel) { dialog, which ->
          dialog.dismiss()
        }
        .create()
  }

  private val booksAdapter = BooksAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.books_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUi()
    showMoviesList()
  }

  private fun setupUi() {
    booksRecyclerView.layoutManager = LinearLayoutManager(context)
    booksRecyclerView.adapter = booksAdapter

    addBookButton.setOnClickListener { addBookDialog.show() }
  }

  override fun onStart() {
    super.onStart()
    database.booksQueries
        .selectAll()
        .addListener(queryListener)
  }

  override fun onStop() {
    super.onStop()
    database.booksQueries.selectAll().removeListener(queryListener)
  }

  private fun showMoviesList() {
    val books = database.booksQueries.selectAll().executeAsList()

    booksAdapter.submitList(books)
    if (books.isEmpty()) {
      Toast.makeText(context, "No books available", Toast.LENGTH_LONG).show()
    }
  }
}
