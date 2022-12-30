package com.ikhwan.townwatchingsemeru.presentation.profile.my_like

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.toPost
import com.ikhwan.townwatchingsemeru.databinding.FragmentMyLikeBinding
import com.ikhwan.townwatchingsemeru.domain.model.Post

class MyLikeFragment : Fragment() {

    private var _binding: FragmentMyLikeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyLikeViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyLikeBinding.inflate(inflater, container, false)
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

    private fun init() {
        viewModel.getToken().observe(viewLifecycleOwner) { auth ->
            if (auth != "") {
                getData(auth)
            }
        }
    }

    private fun getData(auth: String) {
        viewModel.getUserLike(auth).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("MyLikeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("MyLikeFragment", "Loading")
                }
                is Resource.Success -> {
                    result.data?.let { likes ->
                        if (likes.isNotEmpty()) {
                            binding.tvAlertEmptyPost.visibility = View.GONE
                            val posts = mutableListOf<PostDto>()

                            for (d in likes) {
                                posts.add(d.post)
                            }

                            val adapter = MyLikeAdapter {
                                val mBundle = bundleOf(
                                    Constants.EXTRA_ID to it.id,
                                    Constants.EXTRA_FRAGMENT to 1
                                )
                                findNavController().navigate(
                                    R.id.action_profileFragment_to_detailPostFragment,
                                    mBundle
                                )
                            }
                            adapter.submitData(posts)
                            binding.apply {
                                rvLike.adapter = adapter
                                rvLike.layoutManager = GridLayoutManager(requireContext(), 3)

                            }
                        } else {
                            binding.tvAlertEmptyPost.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}