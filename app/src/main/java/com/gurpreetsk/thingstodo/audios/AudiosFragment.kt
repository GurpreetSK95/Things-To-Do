package com.gurpreetsk.thingstodo.audios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gurpreetsk.thingstodo.Injection
import com.gurpreetsk.thingstodo.R
import com.squareup.sqldelight.Query
import kotlinx.android.synthetic.main.audios_fragment.addAudioButton
import kotlinx.android.synthetic.main.audios_fragment.audiosRecyclerView
import kotlinx.android.synthetic.main.audios_fragment.audiosRootView
import java.util.Date

const val TAG_AUDIOS_FRAGMENT = "TAG_AUDIOS_FRAGMENT"

class AudiosFragment : Fragment() {
  companion object {
    fun newInstance(): AudiosFragment {
      return AudiosFragment()
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

  private val addAudioDialog by lazy {
    val dialogView = layoutInflater.inflate(R.layout.layout_input_dialog, null)
    val nameEditText = dialogView.findViewById(R.id.nameEditText) as EditText

    AlertDialog.Builder(requireContext())
        .setTitle(R.string.add_audio)
        .setCancelable(false)
        .setView(dialogView)
        .setPositiveButton(R.string.add) { dialog, which ->
          database.audioQueries
              .insertOne(nameEditText.text.toString(), 0L, Date().time)
              .also {
                dialog.dismiss()
              }
        }
        .setNegativeButton(R.string.cancel) { dialog, which ->
          dialog.dismiss()
        }
        .create()
  }

  private val audiosAdapter = AudiosAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.audios_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUi()
    showMoviesList()
  }

  private fun setupUi() {
    audiosRecyclerView.layoutManager = LinearLayoutManager(context)
    audiosRecyclerView.adapter = audiosAdapter

    addAudioButton.setOnClickListener { addAudioDialog.show() }
  }

  override fun onStart() {
    super.onStart()
    database.audioQueries
        .selectAll()
        .addListener(queryListener)
  }

  override fun onStop() {
    super.onStop()
    database.audioQueries.selectAll().removeListener(queryListener)
  }

  private fun showMoviesList() {
    val audios = database.audioQueries.selectAll().executeAsList()

    audiosAdapter.submitList(audios)
    if (audios.isEmpty()) {
      Toast.makeText(context, "No audios available", Toast.LENGTH_LONG).show()
    }
  }
}
