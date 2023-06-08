package com.ikhwan.townwatchingsemeru.presentation.detail_post

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.scale
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.*
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var dialog: ShowLoadingAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_detailPostFragment_to_profileFragment)
                }
            })

        dialog = ShowLoadingAlertDialog(requireActivity())
        idPost = requireArguments().getInt(Constants.EXTRA_ID)
        val checkFragment = requireArguments().getInt(Constants.EXTRA_FRAGMENT)

        viewModel.getId().observe(viewLifecycleOwner) { userId ->
            idUser = userId
            setDetailPost(idPost, userId)
        }

        viewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
        }

        if (checkFragment == 1) {
            binding.llButtonListPost.visibility = View.GONE
        }

        binding.apply {
            btnDeleteLaporan.setOnClickListener(this@DetailPostFragment)
            btnEditLaporan.setOnClickListener(this@DetailPostFragment)
            btnLike.setOnClickListener(this@DetailPostFragment)
            btnComment.setOnClickListener(this@DetailPostFragment)
        }
    }

    private fun getCurrentLocation(d: Post) {
        val checkSelfPermission = PermissionChecker.checkSelfMapsPermission(requireContext())

        val permissionCoarse = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val permissionFine = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (!checkSelfPermission) {
            if (permissionCoarse && permissionFine) {
                ShowSnackbarPermission().permissionDisabled(requireView(), requireActivity())
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    101
                )
            }
            return
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            googleMap.isMyLocationEnabled = true

            val location = LatLng(
                post!!.latitude.toDouble(),
                post!!.longitude.toDouble()
            )

            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location, 10.0f
                )
            )
            val imageUrl = Constants.BASE_URL + d.category.image
            addMarkerUrl(imageUrl, googleMap, location, d.id)
        }
    }

    private fun addMarkerUrl(imageUrl: String, googleMap: GoogleMap, loc: LatLng, id1: Int) {
        Glide.with(requireContext()).asBitmap().load(imageUrl).into(object :
            CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmap = resource.scale(80, 80)
                val markerMap = googleMap.addMarker(
                    MarkerOptions().position(loc)
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(bitmap)
                        )
                )
                markerMap?.tag = id1
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }


        })
    }

    private fun setDetailPost(idPost: Int, userId: Int) {
        viewModel.getDetailPost(idPost).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
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
                            val datePost = Converter.convertDate(post.updatedAt).split(" ")
                            val txtDate =
                                "${datePost[0]} ${datePost[1]} ${datePost[2]} - ${datePost[3]} WIB"

                            getCurrentLocation(post)
                            Glide.with(requireView()).load(imageUrl).into(ivPost)
                            tvAddress.text = post.address
                            tvDescription.text = post.description
                            Glide.with(requireView()).load(imageUserUrl).into(ivUser)
                            val nameCat = "${post.user.name}"
                            tvUser.text = nameCat

                            if (post.category.id == 4) {
                                tvLevel.visibility = View.GONE
                            } else {
                                tvLevel.visibility = View.VISIBLE
                                val level = post.level

                                tvLevel.text = post.level
                                var cardDrawable = cvLevel.background
                                cardDrawable = DrawableCompat.wrap(cardDrawable)

                                when (level) {
                                    "Ringan" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#00BDAA"))
                                    "Sedang" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#E8AC13"))
                                    "Berat" -> DrawableCompat.setTint(cardDrawable, Color.parseColor("#CF0A0A"))
                                }

                                cvLevel.background = cardDrawable
                            }

                            cardStatusDrawable = DrawableCompat.wrap(cardStatusDrawable)
                            if(post.category.id == 4 && status){
                                textStatus = "Laporan Aktif"
                                DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#00BDAA"))
                            }else if(post.category.id == 4 && !status){
                                textStatus ="Laporan Tidak Aktif"
                                DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#CF0A0A"))
                            }else{
                                if(status){
                                    textStatus ="Laporan Aktif"
                                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#CF0A0A"))
                                }else{
                                    textStatus = "Laporan Tidak Aktif"
                                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#00BDAA"))
                                }
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

        sBLike.append(" Menyimpan Laporan")
        tvLike.text = sBLike.toString()

        for (d in post.like) {
            if (d.userId == userId) {
                btnLike.setBackgroundResource(R.drawable.baseline_bookmark_24)
                likeCheck = true
                break
            }
        }
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
                ShowActionAlertDialog(
                    context = requireContext(),
                    title = "Hapus Laporan",
                    message = "Apakah anda yakin ingin menghapus laporan ini?",
                    positiveButtonAction = {
                        deletePost()
                    }
                ).invoke()
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
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
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

                    getLike()

                    if (message == "Berhasil menghapus like") {
                        binding.btnLike.setBackgroundResource(R.drawable.baseline_bookmark_border_24)
                        Toast.makeText(requireContext(), "Berhasil membatalkan simpan laporan", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.btnLike.setBackgroundResource(R.drawable.baseline_bookmark_24)
                        Toast.makeText(requireContext(), "Berhasil menyimpan laporan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getLike(){
        viewModel.getPostLike(idPost).observe(viewLifecycleOwner){result ->
            when(result){
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Get Like")
                }
                is Resource.Success -> {
                    result.data.let {like ->
                        val sBLike =
                            StringBuilder(like!!.size.toString())

                        sBLike.append(" Menyukai")
                        binding.tvLike.text = sBLike.toString()
                    }
                }
            }
        }
    }

}