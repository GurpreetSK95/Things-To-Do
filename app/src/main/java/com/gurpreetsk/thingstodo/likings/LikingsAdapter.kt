package com.gurpreetsk.thingstodo.likings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gurpreetsk.thingstodo.R
import kotlinx.android.synthetic.main.list_item_likes_dislikes.view.likeDislikeNameTextView
import likes.LikesDislikes

class LikingsAdapter : ListAdapter<LikesDislikes, LikesDislikesViewHolder>(LikesDislikesDiffUtilCallback()) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikesDislikesViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_likes_dislikes, parent, false)

    return LikesDislikesViewHolder(view)
  }

  override fun onBindViewHolder(holder: LikesDislikesViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}

class LikesDislikesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(likeDislike: LikesDislikes) {
    view.likeDislikeNameTextView.text = likeDislike.name
    view.setBackgroundColor(
        ContextCompat.getColor(view.context, when {
          likeDislike.liked == 1L    -> R.color.colorLiked
          likeDislike.disliked == 1L -> R.color.colorDisiked

          else -> R.color.transparent
        })
    )
  }
}

private class LikesDislikesDiffUtilCallback : DiffUtil.ItemCallback<LikesDislikes>() {
  override fun areItemsTheSame(oldItem: LikesDislikes, newItem: LikesDislikes): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: LikesDislikes, newItem: LikesDislikes): Boolean =
      oldItem.name == newItem.name && oldItem.liked == newItem.liked && oldItem.disliked == newItem.disliked
}
