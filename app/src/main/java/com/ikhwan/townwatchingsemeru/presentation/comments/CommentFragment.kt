package com.ikhwan.townwatchingsemeru.presentation.comments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.ShowActionAlertDialog
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentCommentBinding


class CommentFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private var idPost = 0
    private var token = ""
    private var idUser = 0

    private val viewModel: CommentViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var dialog: ShowLoadingAlertDialog

    private fun observeId() {
        viewModel.getId().observe(viewLifecycleOwner) {
            idUser = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        idPost = arguments?.getInt(Constants.EXTRA_ID)!!
        token = arguments?.getString(Constants.EXTRA_TOKEN)!!

        observeId()
        observeGetComment()

        binding.btnComment.setOnClickListener(this)
    }

    private fun observeGetComment() {
        viewModel.getComment(idPost).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("CommentFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("CommentFragment", "Loading Comment")
                }
                is Resource.Success -> {
                    result.data?.let { listComments ->
                        val adapter = CommentAdapter(idUser = idUser,
                            btnDeleteSetOnClick = { idComment ->
                                ShowActionAlertDialog(
                                    context = requireContext(),
                                    title = "Hapus Komentar?",
                                    message = "Anda yakin ingin menghapus komentar?",
                                    positiveButtonAction = {
                                        deleteComment(idComment)
                                    }
                                ).invoke()
                            })
                        if (listComments.isEmpty()) {
                            binding.tvAlertEmptyCategory.visibility = View.VISIBLE
                            adapter.submitData(emptyList())
                            binding.apply {
                                rvComment.adapter = adapter
                                rvComment.layoutManager = LinearLayoutManager(requireContext())
                            }

                        } else {
                            binding.tvAlertEmptyCategory.visibility = View.GONE

                            adapter.submitData(listComments)
                            binding.apply {
                                rvComment.adapter = adapter
                                rvComment.layoutManager = LinearLayoutManager(requireContext())
                            }
                        }

                    }
                }
            }
        }
    }

    private fun deleteComment(idComment: Int) {
        viewModel.deleteComment(token, idComment)
            .observe(viewLifecycleOwner) { result ->
                when(result){
                    is Resource.Error -> {
                        dialog.dismissDialog()
                        Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        dialog.startDialog()
                    }
                    is Resource.Success -> {
                        dialog.dismissDialog()
                        observeGetComment()
                        Toast.makeText(requireContext(), "Komentar dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_comment -> {
                binding.apply {
                    val comment = etComment.text.toString()

                    if (comment.isNotEmpty()) {
                        val commentBody = CommentBody(comment, idPost)
                        observeAddComment(commentBody)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Tuliskan komentar terlebih dahulu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun observeAddComment(commentBody: CommentBody) {
        viewModel.addComment(token, commentBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                    Log.d("CommentFragment", "Loading Add Comment")
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    result.data?.message.let { responseMessage ->
                        Toast.makeText(requireContext(), responseMessage, Toast.LENGTH_SHORT).show()
                        observeGetComment()
                        binding.etComment.setText("")
                    }
                }
            }
        }
    }

}