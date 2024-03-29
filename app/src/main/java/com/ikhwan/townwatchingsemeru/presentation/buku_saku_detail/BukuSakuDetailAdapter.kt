package com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikhwan.townwatchingsemeru.data.local.DetailBencanaDescription
import com.ikhwan.townwatchingsemeru.databinding.ItemBukuSakuDetailBinding

class BukuSakuDetailAdapter(val list: List<DetailBencanaDescription>) :
    RecyclerView.Adapter<BukuSakuDetailAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemBukuSakuDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: DetailBencanaDescription, position: Int){
                binding.apply {
                    tvDescription.text = data.desc
                    tvPosition.text = position.toString()
                    ivDescription.setImageResource(data.img)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemBukuSakuDetailBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], (position + 1))
    }
}
