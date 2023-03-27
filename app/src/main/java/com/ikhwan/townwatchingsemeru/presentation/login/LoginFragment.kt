package com.ikhwan.townwatchingsemeru.presentation.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentLoginBinding


class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var dialog: ShowLoadingAlertDialog

    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })

        binding.apply {
            btnLogin.setOnClickListener(this@LoginFragment)
            btnRegister.setOnClickListener(this@LoginFragment)
            btnLupaPassword.setOnClickListener(this@LoginFragment)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                binding.apply {
                    email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    login(LoginBody(email, password))
                }
            }
            R.id.btn_register -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.btn_lupa_password -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        }
    }

    private fun login(loginBody: LoginBody) {
        viewModel.loginUser(loginBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    dialog.dismissDialog()
                    Log.d("LoginErrorRes", result.toString())
                    Log.d("LoginErrorDat", result.data.toString())
                    Log.d("LoginErrorMes", result.message.toString())
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                    Log.d("LoginFragment", "Loading Login")
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    result.data!!.let { response ->
                        if (response.message.contains("successful")) {
                            viewModel.setId(response.id)
                            viewModel.setToken(response.token)
                            Toast.makeText(requireContext(), "Berhasil Login", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_loginFragment_to_homeFragment)
                        } else if (response.message.contains("Verifikasi")) {
                            val mBundle = bundleOf(Constants.EXTRA_EMAIL to email)
                            Navigation.findNavController(requireView())
                                .navigate(
                                    R.id.action_loginFragment_to_registerVerifyFragment,
                                    mBundle
                                )
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }


}