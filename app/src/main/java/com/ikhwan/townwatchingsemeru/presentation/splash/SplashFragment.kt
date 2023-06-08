package com.ikhwan.townwatchingsemeru.presentation.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        val sharedPref = activity?.getSharedPreferences(Constants.KEY_PREF, Context.MODE_PRIVATE)
        val state = sharedPref?.getBoolean(Constants.KEY_PREF, false) as Boolean

        lifecycleScope.launch {
            delay(2000)
            if (state) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_splashFragment_to_userManualFirstFragment2)
            }
        }
    }
}