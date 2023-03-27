package com.ikhwan.townwatchingsemeru.presentation.buku_saku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.databinding.FragmentBukuSakuBinding


class BukuSakuFragment : Fragment() {

    private var _binding: FragmentBukuSakuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBukuSakuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BukuSakuAdapter { id, category ->
            val mBundle = bundleOf(Constants.EXTRA_ID to id, Constants.EXTRA_CATEGORY to category)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_bukuSakuFragment_to_bukuSakuDetailFragment, mBundle)
        }
        binding.apply {
            rvBukuSaku.layoutManager = LinearLayoutManager(requireContext())
            rvBukuSaku.adapter = adapter
        }
    }
}