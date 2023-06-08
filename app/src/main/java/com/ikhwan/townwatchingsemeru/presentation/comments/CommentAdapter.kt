package com.ikhwan.townwatchingsemeru.presentation.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.utils.Converter
import com.ikhwan.townwatchingsemeru.databinding.ItemCommentBinding
import com.ikhwan.townwatchingsemeru.domain.model.Comment


class CommentAdapter(private val idUser: Int, val btnDeleteSetOnClick: (Int) -> Unit) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Comment) {
            binding.apply {
                val imageUrl = Constants.BASE_URL + data.user.image
                val date = Converter.convertDate(data.createdAt).split(" ")
                val txtDate = "${date[0]} ${date[1]} ${date[2]} - ${date[3]} WIB"
                val txtName = "${data.user.name}"

                tvName.text = txtName
                tvComment.text = data.comment
                tvDate.text = txtDate
                Glide.with(itemView).load(imageUrl).into(ivUser)

                if (idUser == data.user.id) {
                    btnDeleteComment.visibility = View.VISIBLE
                } else {
                    btnDeleteComment.visibility = View.INVISIBLE
                }

                btnDeleteComment.setOnClickListener {
                    btnDeleteSetOnClick(data.id)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Comment>() {

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<Comment>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCommentBinding.inflate(inflater, parent, false))
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