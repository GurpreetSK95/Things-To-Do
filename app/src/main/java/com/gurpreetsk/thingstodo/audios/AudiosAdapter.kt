package com.gurpreetsk.thingstodo.audios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import audio.Audio
import com.gurpreetsk.thingstodo.R
import kotlinx.android.synthetic.main.list_item_audio.view.audioNameTextView
import kotlinx.android.synthetic.main.list_item_movie.view.movieNameTextView

class AudiosAdapter : ListAdapter<Audio, AudioViewHolder>(AudioDiffUtilCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_audio, parent, false)

    return AudioViewHolder(view)
  }

  override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

class AudioViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(audio: Audio) {
    view.audioNameTextView.text = audio.name
    view.setBackgroundColor(ContextCompat.getColor(view.context, if (audio.isConsumed == 1L) {
      R.color.colorConsumedContent
    } else {
      R.color.transparent
    }))
  }
}

private class AudioDiffUtilCallback : DiffUtil.ItemCallback<Audio>() {
  override fun areItemsTheSame(oldItem: Audio, newItem: Audio): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Audio, newItem: Audio): Boolean =
      oldItem.name == newItem.name && oldItem.isConsumed == newItem.isConsumed
}
