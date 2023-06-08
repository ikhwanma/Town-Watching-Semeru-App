package com.ikhwan.townwatchingsemeru.presentation.user_manual

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.databinding.FragmentUserManualThirdBinding


class UserManualThirdFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUserManualThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserManualThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnBack.setOnClickListener(this@UserManualThirdFragment)
            btnNext.setOnClickListener(this@UserManualThirdFragment)
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_back -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_userManualThirdFragment_to_userManualSecondFragment)
            }
            R.id.btn_next -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_userManualThirdFragment_to_homeFragment)
                val sharedPref = activity?.getSharedPreferences(Constants.KEY_PREF, Context.MODE_PRIVATE)!!
                with(sharedPref.edit()){
                    putBoolean(Constants.KEY_PREF, true)
                    apply()
                }
            }
        }
    }

}