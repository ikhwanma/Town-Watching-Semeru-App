package com.ikhwan.townwatchingsemeru.presentation.profile.my_post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.databinding.ItemUserPostBinding
import com.ikhwan.townwatchingsemeru.domain.model.Post

class MyPostAdapter(val onItemClick: (Post) -> Unit) : RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemUserPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Post) {
            binding.apply {

                val imageUrl = Constants.BASE_URL + data.image

                Glide.with(itemView).load(imageUrl).into(binding.ivPost)

                root.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<Post>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemUserPostBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[itemCount - position - 1]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}