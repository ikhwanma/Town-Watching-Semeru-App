package com.ikhwan.townwatchingsemeru.presentation.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetProfileBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private val imageIcon =
        mutableListOf(R.drawable.ic_round_grid_view_24, R.drawable.ic_red_favorite_24)
    private val textAdapter =
        mutableListOf("Laporan Saya", "Laporan Disukai")

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.btnSettings.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {

        viewModel.getToken().observe(viewLifecycleOwner) {
            if (it != "") {
                token = it
                getData(it)
            } else {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment2_to_loginFragment)
            }
        }

        binding.apply {
            viewPager2.adapter = ProfilePagerAdapter(requireParentFragment())

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.setIcon(imageIcon[position])
                tab.text = textAdapter[position]
            }.attach()
        }
    }

    private fun getData(token: String) {
        viewModel.getUser(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("ProfileFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("ProfileFragment", "Loading Profile")
                }
                is Resource.Success -> {
                    result.data!!.let { user ->
                        val imageUrl = Constants.BASE_URL + user.image

                        val sBCategory = StringBuilder("(")
                        sBCategory.append("${user.categoryUser.categoryUser})")

                        binding.apply {
                            Glide.with(requireView()).load(imageUrl).into(imgUser)
                            tvUser.text = user.name
                            tvCategory.text = sBCategory.toString()
                        }
                    }
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_settings -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bindingSheet = BottomSheetProfileBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bindingSheet.root)
                bottomSheetDialog.show()

                bindingSheet.apply {
                    btnLogout.setOnClickListener {
                        viewModel.setId(0)
                        viewModel.setToken("")
                        bottomSheetDialog.dismiss()
                    }
                    btnEditPassword.setOnClickListener {
                        bottomSheetDialog.dismiss()
                        val mBundle = bundleOf(Constants.EXTRA_TOKEN to token)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_profileFragment_to_editPasswordFragment, mBundle)
                    }
                }
            }
        }
    }

}