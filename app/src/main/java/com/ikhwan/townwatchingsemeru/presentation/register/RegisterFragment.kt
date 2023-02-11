package com.ikhwan.townwatchingsemeru.presentation.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.common.utils.Validator
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private val hashMap = HashMap<Int, String>()

    private var checkEmail = false
    private var checkName = false
    private var checkPassword = false

    private lateinit var dialog: ShowLoadingAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        setAdapterDropdown()

        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvAlertEmail.visibility = View.GONE
                checkEmail = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val validateEmail = Validator.emailValidator(p0.toString())
                val txtValidator = "Email tidak sesuai format"

                if (!validateEmail){
                    checkEmail = false
                    binding.tvAlertEmail.visibility = View.VISIBLE
                    binding.tvAlertEmail.text = txtValidator
                }else{
                    checkEmail = true
                    binding.tvAlertEmail.visibility = View.GONE
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.etNama.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvAlertName.visibility = View.GONE
                checkName = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val validateName = Validator.nameValidator(p0.toString())

                if (validateName.isEmpty()){
                    checkName = true
                    binding.tvAlertName.visibility = View.GONE
                }else{
                    checkName = false
                    binding.tvAlertName.visibility = View.VISIBLE
                    binding.tvAlertName.text = validateName
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvAlertPassword.visibility = View.GONE
                checkPassword = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val validatePassword = Validator.passwordValidator(p0.toString())

                if (validatePassword.isEmpty()){
                    checkPassword = true
                    binding.tvAlertPassword.visibility = View.GONE
                }else{
                    checkPassword = false
                    binding.tvAlertPassword.visibility = View.VISIBLE
                    binding.tvAlertPassword.text = validatePassword
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

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

                    if (checkName &&
                        checkEmail &&
                        checkPassword
                    ) {
                        registerUser(name, email, password, categoryUserId)
                    } else if(
                        name.isEmpty() || email.isEmpty() || password.isEmpty()
                    ){
                        Toast.makeText(requireContext(), "Isi semua field", Toast.LENGTH_SHORT).show()
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
                    val message = result.message!!
                    dialog.dismissDialog()
                    if (message.contains("409")){
                        Toast.makeText(requireContext(), "Email telah terdaftar", Toast.LENGTH_SHORT).show()
                    }else{
                        Log.d("RegisterFragment", message)
                    }

                }
                is Resource.Loading -> {
                    dialog.startDialog()
                    Log.d("RegisterFragment", "Loading Register")
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), "Akun didaftarkan", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }


}