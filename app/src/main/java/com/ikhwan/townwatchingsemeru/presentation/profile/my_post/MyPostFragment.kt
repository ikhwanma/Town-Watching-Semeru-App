package com.ikhwan.townwatchingsemeru.presentation.profile.my_post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.databinding.FragmentMyPostBinding

class MyPostFragment : Fragment() {

    private var _binding: FragmentMyPostBinding? = null
    val binding get() = _binding!!

    private val viewModel: MyPostViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onPause() {
        super.onPause()
        init()
    }

    private fun init() {
        viewModel.getToken().observe(viewLifecycleOwner) { auth ->
            if (auth != "") {
                getData(auth)
            }
        }
    }

    private fun getData(auth: String) {
        viewModel.getUserPost(auth).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Log.d("MyPostFragment", "Loading")
                }
                is Resource.Success -> {
                    result.data?.let { posts ->
                        if (posts.isEmpty()) {
                            binding.tvAlertEmptyPost.visibility = View.VISIBLE
                        } else {
                            binding.apply {
                                tvAlertEmptyPost.visibility = View.GONE
                                val adapter = MyPostAdapter { item ->
                                    val mBundle = bundleOf(
                                        Constants.EXTRA_ID to item.id,
                                        Constants.EXTRA_FRAGMENT to 0
                                    )
                                    Navigation.findNavController(requireView()).navigate(
                                        R.id.action_profileFragment_to_detailPostFragment,
                                        mBundle
                                    )
                                }
                                adapter.submitData(posts)

                                rvPost.adapter = adapter
                                rvPost.layoutManager = GridLayoutManager(requireContext(), 3)
                            }
                        }
                    }
                }
            }
        }
    }


}