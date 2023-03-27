package com.ikhwan.townwatchingsemeru.presentation.reset_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.resetpassword.ResetPasswordBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private var checkPassword = false
    private var checkConfirmPassword = false
    private var email = ""

    private val viewModel: ResetPasswordViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var dialog: ShowLoadingAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        email = requireArguments().getString(Constants.EXTRA_EMAIL)!!

        binding.apply {

            etNewPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    tvAlertPassword.visibility = View.GONE
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().length < 6) {
                        tvAlertPassword.visibility = View.VISIBLE
                        checkPassword = false
                    } else {
                        tvAlertPassword.visibility = View.GONE
                        checkPassword = true
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            etConfirmNewPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    tvAlertConfirmPassword.visibility = View.GONE
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val newPassword = etNewPassword.text.toString()
                    if (p0.toString() != newPassword) {
                        tvAlertConfirmPassword.visibility = View.VISIBLE
                        checkConfirmPassword = false
                    } else {
                        tvAlertConfirmPassword.visibility = View.GONE
                        checkConfirmPassword = true
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            btnResetPassword.setOnClickListener(this@ResetPasswordFragment)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_reset_password -> {
                binding.apply {
                    val newPassword = etNewPassword.text.toString()

                    if (!checkPassword || !checkConfirmPassword) {
                        Log.d("checkPassword", "Password salah")
                    } else {
                        editPassword(ResetPasswordBody(email, newPassword))
                    }
                }
            }
        }
    }

    private fun editPassword(resetPasswordBody: ResetPasswordBody) {
        viewModel.resetPassword(resetPasswordBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    Toast.makeText(
                        requireContext(),
                        "Password diubah, silakan mencoba login kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                }
            }
        }
    }


}