package com.gurpreetsk.thingstodo.movies

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
import com.gurpreetsk.thingstodo.books.empty
import com.squareup.sqldelight.Query
import kotlinx.android.synthetic.main.movies_fragment.addMovieButton
import kotlinx.android.synthetic.main.movies_fragment.moviesRecyclerView
import java.util.Date

const val TAG_MOVIES_FRAGMENT = "TAG_MOVIES_FRAGMENT"

class MoviesFragment : Fragment() {
  companion object {
    fun newInstance(): MoviesFragment {
      return MoviesFragment()
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

  private val addMovieDialog by lazy {
    val dialogView = layoutInflater.inflate(R.layout.layout_input_dialog, null)
    val nameEditText = dialogView.findViewById(R.id.nameEditText) as EditText

    AlertDialog.Builder(requireContext())
        .setTitle(R.string.add_movie)
        .setCancelable(false)
        .setView(dialogView)
        .setPositiveButton(R.string.add) { dialog, which ->
          database.movieQueries
              .insertOne(nameEditText.text.toString(), 0L, Date().time)
              .also {
                nameEditText.setText(String.empty())
                dialog.dismiss()
              }
        }
        .setNegativeButton(R.string.cancel) { dialog, which ->
          dialog.dismiss()
        }
        .create()
  }

  private val moviesAdapter = MoviesAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.movies_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUi()
    showMoviesList()
  }

  private fun setupUi() {
    moviesRecyclerView.layoutManager = LinearLayoutManager(context)
    moviesRecyclerView.adapter = moviesAdapter

    addMovieButton.setOnClickListener { addMovieDialog.show() }
  }

  override fun onStart() {
    super.onStart()
    database.movieQueries
        .selectAll()
        .addListener(queryListener)
  }

  override fun onStop() {
    super.onStop()
    database.movieQueries.selectAll().removeListener(queryListener)
  }

  private fun showMoviesList() {
    val movies = database.movieQueries.selectAll().executeAsList()

    moviesAdapter.submitList(movies)
    if (movies.isEmpty()) {
      Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
    }
  }
}
