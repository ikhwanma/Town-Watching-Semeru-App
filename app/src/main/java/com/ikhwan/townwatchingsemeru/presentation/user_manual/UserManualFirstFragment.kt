package com.ikhwan.townwatchingsemeru.presentation.user_manual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.databinding.FragmentUserManualFirstBinding

class UserManualFirstFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUserManualFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserManualFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_next -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_userManualFirstFragment_to_userManualSecondFragment)
            }
        }
    }

}