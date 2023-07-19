package com.ikhwan.townwatchingsemeru.presentation.buku_saku_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.databinding.FragmentBukuSakuDetailBinding
import com.ikhwan.townwatchingsemeru.presentation.profile.ProfilePagerAdapter


class BukuSakuDetailFragment : Fragment() {

    private var _binding: FragmentBukuSakuDetailBinding? = null
    private val binding get() = _binding!!

    private val textAdapter =
        mutableListOf("Deskripsi", "PraBencana", "Saat Bencana", "Pasca Bencana")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBukuSakuDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = requireArguments().getString(Constants.EXTRA_CATEGORY)
        val id = requireArguments().getInt(Constants.EXTRA_ID)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_bukuSakuDetailFragment_to_bukuSakuFragment)
                }
            })

        binding.apply {
            viewPager2.adapter = BukuSakuDetailPagerAdapter(this@BukuSakuDetailFragment, id)

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = textAdapter[position]
            }.attach()
        }
        binding.tvBencana.text = category
    }

}