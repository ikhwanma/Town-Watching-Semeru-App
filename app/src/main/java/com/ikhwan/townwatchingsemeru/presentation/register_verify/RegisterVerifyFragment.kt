package com.ikhwan.townwatchingsemeru.presentation.register_verify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.ShowActionAlertDialog
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.ResendCodeBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.VerifyCodeBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentRegisterVerifyBinding


class RegisterVerifyFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterVerifyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterVerifyViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var token = ""
    private var email = ""

    private lateinit var dialog: ShowLoadingAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        email = requireArguments().getString(Constants.EXTRA_EMAIL)!!

        binding.etPin.setOnPinEnteredListener { str ->
            token = str.toString()
        }

        binding.btnVerification.setOnClickListener(this)
        binding.btnResendCode.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_verification -> {
                if (token != "") {
                    verifyCode()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Isi field terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.btn_resend_code -> {
                ShowActionAlertDialog(
                    context = requireContext(),
                    title = "Kirim ulang kode",
                    message = "Apakah anda yakin ingin mengirim ulang kode registrasi?",
                    positiveButtonAction = {
                        viewModel.resendRegisterCode(ResendCodeBody(email))
                            .observe(viewLifecycleOwner) { result ->
                                when (result) {
                                    is Resource.Error -> {
                                        dialog.dismissDialog()
                                        Toast.makeText(
                                            requireContext(),
                                            result.message!!,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    is Resource.Loading -> {
                                        dialog.startDialog()
                                    }
                                    is Resource.Success -> {
                                        dialog.dismissDialog()
                                        Toast.makeText(
                                            requireContext(),
                                            result.data!!.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                    }
                ).invoke()
            }
        }
    }

    private fun verifyCode(){
        val verifyCodeBody = VerifyCodeBody(email, token.toInt())
        viewModel.verifyRegister(verifyCodeBody)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        dialog.dismissDialog()
                        val message = result.message!!
                        if (message.contains("400")) {
                            Toast.makeText(
                                requireContext(),
                                "Kode salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is Resource.Loading -> {
                        dialog.startDialog()
                    }
                    is Resource.Success -> {
                        dialog.dismissDialog()
                        Toast.makeText(
                            requireContext(),
                            "Berhasil verifikasi akun, silakan mencoba untuk masuk",
                            Toast.LENGTH_SHORT
                        ).show()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_registerVerifyFragment_to_loginFragment)
                    }
                }
            }
    }

}