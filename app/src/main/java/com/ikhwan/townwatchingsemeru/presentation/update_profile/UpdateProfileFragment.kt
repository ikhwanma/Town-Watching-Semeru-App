package com.ikhwan.townwatchingsemeru.presentation.update_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.UpdateProfileBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentUpdateProfileBinding


class UpdateProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UpdateProfileViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var name = ""
    private var email = ""
    private var token = ""
    private var categoryUserId = 0

    private val hashMap = HashMap<Int, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = arguments?.getString(Constants.EXTRA_NAME)!!
        email = arguments?.getString(Constants.EXTRA_EMAIL)!!
        token = arguments?.getString(Constants.EXTRA_TOKEN)!!
        categoryUserId = arguments?.getInt(Constants.EXTRA_CATEGORY)!!

        setAdapterDropdown()

        binding.apply {
            etNama.setText(name)
            etEmail.isEnabled = false
            etEmail.hint = email
            btnUpdateProfile.setOnClickListener(this@UpdateProfileFragment)
        }
    }

    private fun setAdapterDropdown() {
        viewModel.getCategoryUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("UpdateProfileFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("UpdateProfileFragment", "Loading Category")
                }
                is Resource.Success -> {
                    result.data!!.let {
                        val listCategory = mutableListOf<String>()
                        var position = 0

                        for ((i, d) in it.withIndex()) {
                            listCategory.add(d.categoryUser)
                            hashMap[d.id] = d.categoryUser
                            if (d.id == categoryUserId) position = i
                        }

                        val arrayAdapter =
                            ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listCategory)
                        binding.autoCompleteBencana.setText(arrayAdapter.getItem(position))
                        binding.autoCompleteBencana.setAdapter(arrayAdapter)

                    }
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_update_profile -> {
                binding.apply {
                    val name = etNama.text.toString()
                    val categoryUser = autoCompleteBencana.text.toString()
                    for (key in hashMap.keys) {
                        if (categoryUser == hashMap[key]) {
                            categoryUserId = key
                            break
                        }
                    }
                    if (name.isNotEmpty() ||
                        categoryUser.isNotEmpty()
                    ) {
                        updateProfile(name, categoryUserId)
                    } else {
                        Toast.makeText(requireContext(), "Isi semua field", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun updateProfile(name: String, categoryUserId: Int) {
        val updateProfileBody = UpdateProfileBody(name, categoryUserId)
        viewModel.updateProfile(token, updateProfileBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("UpdateProfileFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("UpdateProfileFragment", "Loading Update")
                }
                is Resource.Success -> {
                    result.data?.message.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_updateProfileFragment_to_profileFragment)
                    }
                }
            }
        }
    }


}