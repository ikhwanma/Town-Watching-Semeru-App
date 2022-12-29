package com.ikhwan.townwatchingsemeru.presentation.detail_post

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.Converter
import com.ikhwan.townwatchingsemeru.databinding.FragmentDetailPostBinding
import com.ikhwan.townwatchingsemeru.domain.model.Post


class DetailPostFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailPostViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var likeSum = 0
    private var likeCheck = false
    private var token = ""
    private var idUser = 0
    private var idPost = 0

    private var post: Post? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idPost = requireArguments().getInt(Constants.EXTRA_ID)

        viewModel.getId().observe(viewLifecycleOwner) { userId ->
            idUser = userId
            setDetailPost(idPost, userId)
        }

        viewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
        }

        binding.apply {
            btnDeleteLaporan.setOnClickListener(this@DetailPostFragment)
            btnEditLaporan.setOnClickListener(this@DetailPostFragment)
            btnLike.setOnClickListener(this@DetailPostFragment)
            btnComment.setOnClickListener(this@DetailPostFragment)
        }
    }

    private fun setDetailPost(idPost: Int, userId: Int) {
        viewModel.getDetailPost(idPost).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.e("DetailPostFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("DetailPostFragment", "Loading detailpost")
                }
                is Resource.Success -> {
                    result.data!!.let { post ->
                        binding.apply {
                            this@DetailPostFragment.post = post
                            val imageUrl = Constants.BASE_URL + post.image
                            val imageUserUrl = Constants.BASE_URL + post.user.image
                            var cardStatusDrawable = cvStatus.background
                            val status = post.status
                            var textStatus = ""
                            val datePost = Converter.convertDate(post.createdAt).split(" ")
                            val txtDate =
                                "${datePost[0]} ${datePost[1]} ${datePost[2]} - ${datePost[3]} WIB"

                            Glide.with(requireView()).load(imageUrl).into(ivPost)
                            setAddress(
                                tvAddress,
                                post.latitude.toDouble(),
                                post.longitude.toDouble()
                            )
                            tvDescription.text = post.description
                            Glide.with(requireView()).load(imageUserUrl).into(ivUser)
                            tvUser.text = post.user.name

                            if (post.category.id == 3 || post.category.id == 4) {
                                tvLevel.visibility = View.GONE
                            } else {
                                tvLevel.visibility = View.VISIBLE
                                val level = post.level

                                tvLevel.text = post.level
                                var cardDrawable = cvLevel.background
                                cardDrawable = DrawableCompat.wrap(cardDrawable)

                                when (level) {
                                    "Ringan" -> DrawableCompat.setTint(
                                        cardDrawable,
                                        Color.parseColor("#14ff00")
                                    )
                                    "Sedang" -> DrawableCompat.setTint(
                                        cardDrawable,
                                        Color.parseColor("#f8e158")
                                    )
                                    "Berat" -> DrawableCompat.setTint(
                                        cardDrawable,
                                        Color.parseColor("#FF0000")
                                    )
                                }

                                cvLevel.background = cardDrawable
                            }

                            cardStatusDrawable = DrawableCompat.wrap(cardStatusDrawable)
                            if ((post.category.id == 1 || post.category.id == 2) && status) {
                                textStatus = "Aktif"
                                DrawableCompat.setTint(
                                    cardStatusDrawable,
                                    Color.parseColor("#FF0000")
                                )
                            } else if ((post.category.id == 1 || post.category.id == 2) && !status) {
                                textStatus = "Tidak Aktif"
                                DrawableCompat.setTint(
                                    cardStatusDrawable,
                                    Color.parseColor("#14ff00")
                                )
                            } else if ((post.category.id == 3 || post.category.id == 4) && status) {
                                textStatus = "Aktif"
                                DrawableCompat.setTint(
                                    cardStatusDrawable,
                                    Color.parseColor("#14ff00")
                                )
                            } else if ((post.category.id == 3 || post.category.id == 4) && !status) {
                                textStatus = "Tidak Aktif"
                                DrawableCompat.setTint(
                                    cardStatusDrawable,
                                    Color.parseColor("#FF0000")
                                )
                            }

                            tvBencana.text = post.category.category
                            tvStatus.text = textStatus
                            cvStatus.background = cardStatusDrawable
                            tvComment.text = post.comment.size.toString()
                            tvDate.text = txtDate

                            likeChecker(post, btnLike, tvLike, userId)
                        }
                    }
                }
            }
        }
    }

    private fun likeChecker(
        post: Post,
        btnLike: ImageView,
        tvLike: TextView,
        userId: Int
    ) {

        likeSum = post.like.size
        val sBLike =
            StringBuilder(likeSum.toString())

        sBLike.append(" Menyukai")
        tvLike.text = sBLike.toString()

        for (d in post.like) {
            if (d.userId == userId) {
                btnLike.setBackgroundResource(R.drawable.ic_red_favorite_24)
                likeCheck = true
                break
            }
        }
    }

    private fun setAddress(tvAddress: TextView, lat: Double, lng: Double) {
        Thread(kotlinx.coroutines.Runnable {
            val address =
                Converter.convertAddress(requireContext(), lat, lng)
            tvAddress.post(kotlinx.coroutines.Runnable {
                tvAddress.text = address
            })
        }).start()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_edit_laporan -> {
                val mBundle = bundleOf(
                    Constants.EXTRA_ID to idPost,
                    Constants.EXTRA_CATEGORY to post!!.category.id,
                    Constants.EXTRA_LEVEL to post!!.level,
                    Constants.EXTRA_TOKEN to token,
                    Constants.EXTRA_IMAGE to post!!.image,
                    Constants.EXTRA_DESCRIPTION to post!!.description,
                    Constants.EXTRA_STATUS to post!!.status
                )
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_detailPostFragment_to_updatePostFragment, mBundle)
            }
            R.id.btn_delete_laporan -> {
                deletePost()
            }
            R.id.btn_like -> {
                addLike()
            }
            R.id.btn_comment -> {
                val mBundle = bundleOf(Constants.EXTRA_ID to idPost, Constants.EXTRA_TOKEN to token)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_detailPostFragment_to_commentFragment, mBundle)
            }
        }
    }

    private fun deletePost() {
        viewModel.deletePostUser(token, idPost).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    result.message.let { message ->
                        if (message!!.contains("500")) {
                            Toast.makeText(requireContext(), "Berhasil dihapus", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_detailPostFragment_to_profileFragment)
                        } else {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Loading -> {
                    Log.d("DetailPostFragment", "Loading Delete")
                }
                is Resource.Success -> {
                    result.data!!.message.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_detailPostFragment_to_profileFragment)
                    }
                }
            }
        }
    }

    private fun addLike() {
        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim)

        binding.btnLike.startAnimation(animation)

        viewModel.addLike(token, idPost).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Category UserDto")
                }
                is Resource.Success -> {
                    val message = result.data!!.message

                    if (message == "Berhasil menghapus like") {
                        likeSum--
                        val sBLike =
                            StringBuilder(likeSum.toString())

                        sBLike.append(" Menyukai")
                        binding.tvLike.text = sBLike.toString()
                        binding.btnLike.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)

                    } else {
                        likeSum++

                        val sBLike =
                            StringBuilder(likeSum.toString())

                        sBLike.append(" Menyukai")
                        binding.tvLike.text = sBLike.toString()

                        binding.btnLike.setBackgroundResource(R.drawable.ic_red_favorite_24)
                    }

                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}