package com.ikhwan.townwatchingsemeru.presentation.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.ResendCodeBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentForgotPasswordBinding


class ForgotPasswordFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: ShowLoadingAlertDialog

    private val viewModel: ForgotPasswordViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        binding.btnNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_next -> {
                val email = binding.etEmail.text.toString()

                if (email.isNotEmpty()) {
                    viewModel.sendCode(ResendCodeBody(email))
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Resource.Error -> {
                                    dialog.dismissDialog()
                                    if (result.message!!.contains("401")) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Pengguna tidak ditemukan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                is Resource.Loading -> {
                                    dialog.startDialog()
                                }
                                is Resource.Success -> {
                                    dialog.dismissDialog()
                                    val mBundle = bundleOf(Constants.EXTRA_EMAIL to email)
                                    Toast.makeText(
                                        requireContext(),
                                        result.data!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Navigation.findNavController(requireView())
                                        .navigate(
                                            R.id.action_forgotPasswordFragment_to_forgotPasswordVerifyFragment,
                                            mBundle
                                        )
                                }
                            }
                        }
                }
            }
        }
    }

}