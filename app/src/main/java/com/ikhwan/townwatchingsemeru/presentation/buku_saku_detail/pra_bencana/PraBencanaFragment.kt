package com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.pra_bencana

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.data.local.getPraBencanaData
import com.ikhwan.townwatchingsemeru.databinding.FragmentPraBencanaBinding
import com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail.BukuSakuDetailAdapter


class PraBencanaFragment(private val idCategory: Int) : Fragment() {

    private var _binding: FragmentPraBencanaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPraBencanaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = getPraBencanaData().filter {it.id == idCategory }
        val adapter = BukuSakuDetailAdapter(listData[0].desc)

        binding.apply {
            rvPrabencana.adapter = adapter
            rvPrabencana.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}