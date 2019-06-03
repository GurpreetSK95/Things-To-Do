package com.gurpreetsk.thingstodo.misc

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
import kotlinx.android.synthetic.main.misc_fragment.addMiscButton
import kotlinx.android.synthetic.main.misc_fragment.miscRecyclerView
import java.util.Date

const val TAG_MISC_FRAGMENT = "TAG_MISC_FRAGMENT"

class MiscFragment : Fragment() {
  companion object {
    fun newInstance(): MiscFragment {
      return MiscFragment()
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

  private val addMiscInfoDialog by lazy {
    val dialogView = layoutInflater.inflate(R.layout.layout_info_input_dialog, null)
    val nameEditText  = dialogView.findViewById(R.id.miscNameEditText) as EditText
    val notesEditText = dialogView.findViewById(R.id.miscNotesEditText) as EditText

    AlertDialog.Builder(requireContext())
        .setTitle(R.string.add_misc_info)
        .setCancelable(false)
        .setView(dialogView)
        .setPositiveButton(R.string.add) { dialog, which ->
          database.miscQueries
              .insertOne(nameEditText.text.toString(), notesEditText.text.toString(), Date().time)
              .also {
                nameEditText.setText(String.empty())
                notesEditText.setText(String.empty())
                dialog.dismiss()
              }
        }
        .setNegativeButton(R.string.cancel) { dialog, which ->
          dialog.dismiss()
        }
        .create()
  }

  private val miscAdapter = MiscAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.misc_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUi()
    showMoviesList()
  }

  private fun setupUi() {
    miscRecyclerView.layoutManager = LinearLayoutManager(context)
    miscRecyclerView.adapter = miscAdapter

    addMiscButton.setOnClickListener { addMiscInfoDialog.show() }
  }

  override fun onStart() {
    super.onStart()
    database.miscQueries
        .selectAll()
        .addListener(queryListener)
  }

  override fun onStop() {
    super.onStop()
    database.miscQueries.selectAll().removeListener(queryListener)
  }

  private fun showMoviesList() {
    val books = database.miscQueries.selectAll().executeAsList()

    miscAdapter.submitList(books)
    if (books.isEmpty()) {
      Toast.makeText(context, "No information available", Toast.LENGTH_LONG).show()
    }
  }
}
