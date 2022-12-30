package com.ikhwan.townwatchingsemeru.presentation.edit_password

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
import com.ikhwan.townwatchingsemeru.databinding.FragmentEditPasswordBinding

class EditPasswordFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditPasswordBinding? = null
    private val binding get() = _binding!!

    private var checkPassword = false
    private var checkConfirmPassword = false
    private var token = ""

    private val viewModel: EditPasswordViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var dialog: ShowLoadingAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        token = arguments?.getString(Constants.EXTRA_TOKEN)!!

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

            btnEditPassword.setOnClickListener(this@EditPasswordFragment)
            btnBack.setOnClickListener(this@EditPasswordFragment)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_edit_password -> {
                binding.apply {
                    val password = etCurrentPassword.text.toString()
                    val newPassword = etNewPassword.text.toString()

                    if (password == "") {
                        Toast.makeText(
                            requireContext(),
                            "Password sekarang tidak boleh kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!checkPassword || !checkConfirmPassword) {
                        Log.d("checkPassword", "Password salah")
                    } else {
                        editPassword(EditPasswordBody(password, newPassword))
                    }
                }
            }
            R.id.btn_back -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_editPasswordFragment_to_profileFragment)
            }
        }
    }

    private fun editPassword(editPasswordBody: EditPasswordBody) {
        viewModel.editPassword(token, editPasswordBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                    Log.d("EditPasswordFragment", "Loading")
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    result.data?.message.let { response ->
                        if (response!!.contains("salah")) {
                            Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_editPasswordFragment_to_profileFragment)
                        }
                    }
                }
            }
        }
    }

}