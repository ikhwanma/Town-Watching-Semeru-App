package com.ikhwan.townwatchingsemeru.presentation.buku_saku

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikhwan.townwatchingsemeru.data.local.BukuSakuData
import com.ikhwan.townwatchingsemeru.data.local.getBukuSakuData
import com.ikhwan.townwatchingsemeru.databinding.ItemBukuSakuBinding

class BukuSakuAdapter(val itemOnClick: (Int, String) -> Unit) :
    RecyclerView.Adapter<BukuSakuAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemBukuSakuBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(data: BukuSakuData){
                binding.apply {
                    tvName.text = data.category
                    tvDescription.text = data.description
                    ivCategory.setImageResource(data.icon)

                    root.setOnClickListener {
                        itemOnClick(data.id, data.category)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemBukuSakuBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return getBukuSakuData().size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getBukuSakuData()[position])
    }
}