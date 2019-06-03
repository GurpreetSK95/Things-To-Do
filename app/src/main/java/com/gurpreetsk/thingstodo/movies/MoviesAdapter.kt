package com.gurpreetsk.thingstodo.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gurpreetsk.thingstodo.R
import kotlinx.android.synthetic.main.list_item_movie.view.movieNameTextView
import movie.Movie

class MoviesAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffUtilCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_movie, parent, false)

    return MovieViewHolder(view)
  }

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(movie: Movie) {
    view.movieNameTextView.text = movie.name
    view.setBackgroundColor(ContextCompat.getColor(view.context, if (movie.isWatched == 1L) {
      R.color.colorConsumedContent
    } else {
      R.color.transparent
    }))
  }
}

private class MovieDiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
  override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
      oldItem.name == newItem.name && oldItem.isWatched == newItem.isWatched
}
