package com.ikhwan.townwatchingsemeru.presentation.profile.my_post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.databinding.FragmentMyPostBinding


class MyPostFragment : Fragment() {

    private var _binding : FragmentMyPostBinding? = null
    val binding get() = _binding!!

    private val viewModel: MyPostViewModel by hiltNavGraphViewModels(R.id.nav_main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getToken().observe(viewLifecycleOwner){ auth ->
            if(auth != ""){
                getData(auth)
            }
        }
    }

    private fun getData(auth: String) {
        viewModel.getUserPost(auth).observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Error -> {
                    Log.d("MyPostFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("MyPostFragment", "Loading")
                }
                is Resource.Success -> {
                    result.data!!.let { posts ->
                        binding.apply {
                            val adapter = MyPostAdapter{ item ->
                                Log.d("MyPostFragment", item.toString())
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