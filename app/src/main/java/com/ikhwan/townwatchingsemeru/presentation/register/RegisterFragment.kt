package com.ikhwan.townwatchingsemeru.presentation.register

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
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private val hashMap = HashMap<Int, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapterDropdown()
        binding.btnRegister.setOnClickListener(this)
    }

    private fun setAdapterDropdown() {
        viewModel.getCategoryUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("RegisterFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("RegisterFragment", "Loading Category")
                }
                is Resource.Success -> {
                    result.data!!.let {
                        val listCategory = mutableListOf<String>()

                        for (d in it) {
                            listCategory.add(d.categoryUser)
                            hashMap[d.id] = d.categoryUser
                        }

                        val arrayAdapter =
                            ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listCategory)
                        binding.autoCompleteBencana.setAdapter(arrayAdapter)
                    }
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_register -> {
                binding.apply {
                    val name = etNama.text.toString()
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    val categoryUser = autoCompleteBencana.text.toString()
                    var categoryUserId = 0
                    for (key in hashMap.keys) {
                        if (categoryUser == hashMap[key]) {
                            categoryUserId = key
                            break
                        }
                    }

                    if (name.isNotEmpty() ||
                        email.isNotEmpty() ||
                        password.isNotEmpty() ||
                        categoryUser.isNotEmpty()
                    ) {
                        registerUser(name, email, password, categoryUserId)
                    } else {
                        Toast.makeText(requireContext(), "Isi semua field", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String, categoryUserId: Int) {
        val registerBody = RegisterBody(
            name = name,
            email = email,
            password = password,
            categoryUserId = categoryUserId
        )
        viewModel.registerUser(registerBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("RegisterFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("RegisterFragment", "Loading Register")
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Akun didaftarkan", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }


}