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
                val imageCategory = baseUrl + data.category.image
                val status = data.status
                var cardStatusDrawable = cvStatus.background
                val sBLike = StringBuilder(data.like.size.toString())
                var textStatus = ""
                val datePost = Converter.convertDate(data.updatedAt).split(" ")
                val txtCate = "${datePost[0]} ${datePost[1]} ${datePost[2]}"
                val txtTime = "- ${datePost[3]} WIB"
                val addr = data.address.split(",")
                val addrSize = addr.size
                var txtAddress = ""
                for (i in 4 downTo 3){
                    txtAddress += if (i == 4){
                        addr[addrSize-i] + ","
                    }else{
                        addr[addrSize-i]
                    }
                }

                if(data.updatedAt != data.createdAt){
                    tvUpdated.visibility = View.VISIBLE
                }else{
                    tvUpdated.visibility = View.GONE
                }

                sBLike.append(" Menyimpan Laporan")
                val sLike = sBLike.toString()

                tvAddress.text = txtAddress
                tvUser.text = data.user.name

                if (data.category.id == 4){
                    cvLevel.visibility = View.GONE
                    tvLevel.visibility = View.GONE
                }else{
                    tvLevel.visibility = View.VISIBLE
                    val level = data.level

                    tvLevel.append(" ${data.level}")
                    var cardDrawable = cvLevel.background
                    cardDrawable = DrawableCompat.wrap(cardDrawable)

                    when(level){
                        "Ringan" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#00BDAA"))
                        "Sedang" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#E8AC13"))
                        "Berat" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#CF0A0A"))
                    }

                    cvLevel.background = cardDrawable
                }

                cardStatusDrawable = DrawableCompat.wrap(cardStatusDrawable)

                if(data.category.id == 4 && status){
                    textStatus = "Laporan Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#00BDAA"))
                }else if(data.category.id == 4 && !status){
                    textStatus ="Laporan Tidak Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#CF0A0A"))
                }else{
                    if(status){
                        textStatus ="Laporan Aktif"
                        DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#CF0A0A"))
                    }else{
                        textStatus = "Laporan Tidak Aktif"
                        DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#00BDAA"))
                    }
                }

                btnLike.setBackgroundResource(R.drawable.baseline_bookmark_border_24)

                for (d in data.like) {
                    if (d.userId == idUser) {
                        btnLike.setBackgroundResource(R.drawable.baseline_bookmark_24)
                        break
                    }
                }

                Glide.with(itemView).load(imageUrl).into(binding.ivPost)
                Glide.with(itemView).load(imageUser).into(binding.ivUser)
                Glide.with(itemView).load(imageCategory).into(imgCategory)
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
            val dateFirst = data.updatedAt.split(" ")
            val dataNext = differ.currentList[itemCount - position]
            val dateSecond = dataNext.updatedAt.split(" ")
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