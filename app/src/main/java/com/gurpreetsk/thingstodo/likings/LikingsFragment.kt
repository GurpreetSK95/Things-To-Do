package com.gurpreetsk.thingstodo.likings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gurpreetsk.thingstodo.Injection
import com.gurpreetsk.thingstodo.R
import com.gurpreetsk.thingstodo.books.empty
import com.squareup.sqldelight.Query
import kotlinx.android.synthetic.main.likings_fragment.likingsRecyclerView
import kotlinx.android.synthetic.main.movies_fragment.addMovieButton
import java.util.Date

const val TAG_LIKINGS_FRAGMENT = "TAG_LIKINGS_FRAGMENT"

class LikingsFragment : Fragment() {
  companion object {
    fun newInstance(): LikingsFragment {
      return LikingsFragment()
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

  private val addLikingsDialog by lazy {
    val dialogView = layoutInflater.inflate(R.layout.layout_likings_input_dialog, null)
    val nameEditText = dialogView.findViewById(R.id.nameEditText) as EditText
    val likeRadioButton    = dialogView.findViewById(R.id.likeRadioButton) as RadioButton
    val dislikeRadioButton = dialogView.findViewById(R.id.dislikeRadioButton) as RadioButton

    AlertDialog.Builder(requireContext())
        .setTitle(R.string.add_movie)
        .setCancelable(false)
        .setView(dialogView)
        .setPositiveButton(R.string.add) { dialog, which ->
          val liked    = if (likeRadioButton.isChecked) 1L else 0L
          val disliked = if (dislikeRadioButton.isChecked) 1L else 0L

          database.likesDislikesQueries
              .insertOne(nameEditText.text.toString(), liked, disliked, Date().time)
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

  private val likingsAdapter = LikingsAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.likings_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUi()
    showMoviesList()
  }

  private fun setupUi() {
    likingsRecyclerView.layoutManager = LinearLayoutManager(context)
    likingsRecyclerView.adapter = likingsAdapter

    addMovieButton.setOnClickListener { addLikingsDialog.show() }
  }

  override fun onStart() {
    super.onStart()
    database.likesDislikesQueries
        .selectAll()
        .addListener(queryListener)
  }

  override fun onStop() {
    super.onStop()
    database.likesDislikesQueries.selectAll().removeListener(queryListener)
  }

  private fun showMoviesList() {
    val likings = database.likesDislikesQueries.selectAll().executeAsList()

    likingsAdapter.submitList(likings)
    if (likings.isEmpty()) {
      Toast.makeText(context, "Uhmmm...", Toast.LENGTH_LONG).show()
    }
  }
}
