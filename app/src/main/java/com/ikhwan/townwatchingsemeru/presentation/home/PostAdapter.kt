package com.ikhwan.townwatchingsemeru.presentation.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.utils.Converter
import com.ikhwan.townwatchingsemeru.databinding.ItemPostBinding
import com.ikhwan.townwatchingsemeru.domain.model.Post

class PostAdapter(
    val idUser: Int,
    val context: Context,
    val setAddress: (TextView, Double, Double) -> Unit,
    val setCategoryUser: (TextView, Int, String) -> Unit,
    val onCommentClick: (Int) -> Unit,
    val onLikeClick: (Post, ImageView, TextView) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Post, check: Boolean) {
            binding.apply {

                val baseUrl = Constants.BASE_URL
                val imageUrl = baseUrl + data.image
                val imageUser = baseUrl + data.user.image
                val status = data.status
                var cardStatusDrawable = cvStatus.background
                val sBLike = StringBuilder(data.like.size.toString())
                var textStatus = ""
                val datePost = Converter.convertDate(data.createdAt).split(" ")
                val txtCate = "${datePost[0]} ${datePost[1]} ${datePost[2]}"
                val txtTime = "- ${datePost[3]} WIB"
                sBLike.append(" Menyukai")
                val sLike = sBLike.toString()

                setAddress(tvAddress, data.latitude.toDouble(), data.longitude.toDouble())
                setCategoryUser(tvUser, data.user.categoryUserId, data.user.name)

                if (check){
                    tvDate.visibility = View.VISIBLE
                    cvIconBlue.visibility = View.VISIBLE
                }else{
                    tvDate.visibility = View.GONE
                    cvIconBlue.visibility = View.GONE
                }

                if (data.category.id == 3 || data.category.id == 4){
                    tvLevel.visibility = View.GONE
                }else{
                    tvLevel.visibility = View.VISIBLE
                    val level = data.level

                    tvLevel.text = data.level
                    var cardDrawable = cvLevel.background
                    cardDrawable = DrawableCompat.wrap(cardDrawable)

                    when(level){
                        "Ringan" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#14ff00"))
                        "Sedang" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#f8e158"))
                        "Berat" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#FF0000"))
                    }

                    cvLevel.background = cardDrawable
                }

                cardStatusDrawable = DrawableCompat.wrap(cardStatusDrawable)

                if ( (data.category.id == 1 || data.category.id == 2)&& status){
                    textStatus ="Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#FF0000"))
                }else if ((data.category.id == 1 || data.category.id == 2) && !status){
                    textStatus = "Tidak Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#14ff00"))
                }else if((data.category.id == 3 || data.category.id == 4) && status){
                    textStatus = "Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#14ff00"))
                }else if((data.category.id == 3 || data.category.id == 4) && !status){
                    textStatus ="Tidak Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#FF0000"))
                }
                btnLike.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)

                for (d in data.like) {
                    if (d.userId == idUser) {
                        btnLike.setBackgroundResource(R.drawable.ic_red_favorite_24)
                        break
                    }
                }

                Glide.with(itemView).load(imageUrl).into(binding.ivPost)
                Glide.with(itemView).load(imageUser).into(binding.ivUser)
                tvDescription.text = data.description
                tvBencana.text = data.category.category
                tvStatus.text = textStatus
                cvStatus.background = cardStatusDrawable
                tvDate.text = txtCate
                tvTime.text = txtTime
                tvLike.text = sLike
                tvComment.text = data.comment.size.toString()

                btnLike.setOnClickListener {
                    onLikeClick(data, btnLike, tvLike)
                }

                btnComment.setOnClickListener {
                    onCommentClick(data.id)
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
        return ViewHolder(ItemPostBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == 0 || position == itemCount){
            val data = differ.currentList[itemCount - 1]
            data.let {
                holder.bind(data, true)
            }
        }else{
            val data = differ.currentList[itemCount - position - 1]
            val dateFirst = data.createdAt.split(" ")
            val dataNext = differ.currentList[itemCount - position]
            val dateSecond = dataNext.createdAt.split(" ")
            val check = dateFirst[0] != dateSecond[0]
            data.let {
                holder.bind(data, check)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}