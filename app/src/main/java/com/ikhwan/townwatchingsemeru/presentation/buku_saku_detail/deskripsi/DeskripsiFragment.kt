package com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.deskripsi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.data.local.getDeskripsi
import com.ikhwan.townwatchingsemeru.data.local.getPraBencanaData
import com.ikhwan.townwatchingsemeru.databinding.FragmentDeskripsiBinding
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.BukuSakuDetailAdapter

class DeskripsiFragment(private val idCategory: Int) : Fragment() {

    private var _binding: FragmentDeskripsiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeskripsiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = getDeskripsi().filter {it.id == idCategory }

        binding.apply {
            tvDesc.text = listData[0].desc.desc
            ivDesc.setImageResource(listData[0].desc.img)
        }
    }

}