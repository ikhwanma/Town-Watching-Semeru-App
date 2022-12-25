package com.ikhwan.townwatchingsemeru.presentation.maps

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.utils.BitmapDescriptor
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.Converter
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetMapsBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentMapsBinding
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.common.utils.PermissionChecker

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapsViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private var listPosts: List<Post> = emptyList()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach { map ->
            if (map.value) {
                getCurrentLocation()
            } else {
                PermissionChecker().checkMapsPermission(requireContext(), requireActivity())
            }
        }
    }

    private fun observeCategory() = lifecycleScope.launchWhenStarted {
        viewModel.getCategory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("MapsFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("MapsFragment", "Loading Category")
                }
                is Resource.Success -> {
                    val data = result.data!!
                    val listCategory = mutableListOf("Lihat Semua")
                    for (d in data){
                        listCategory.add(d.category)
                    }
                    val adapterBencana =
                        ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listCategory)
                    binding.autoCompleteBencana.setAdapter(adapterBencana)
                    binding.textInputLayout.setStartIconDrawable(R.drawable.ic_round_grid_view_24)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCategory()
        init()
    }

    override fun onPause() {
        super.onPause()
        binding.autoCompleteBencana.text = null
    }


    private fun init(){
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getCurrentLocation() {
        val checkSelfPermission = PermissionChecker().checkSelfMapsPermission(requireContext())
        if (!checkSelfPermission) return

        val semeru = LatLng(-8.2061557, 112.9773703)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            googleMap.isMyLocationEnabled = true

            viewModel.getAllPosts().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.d("MapsFragment", result.message!!)
                    }
                    is Resource.Loading -> {
                        Log.d("MapsFragment", "Loading Post")
                    }
                    is Resource.Success -> {
                        val data = result.data!!

                        listPosts = data

                        var category = binding.autoCompleteBencana.text.toString()

                        binding.autoCompleteBencana.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                googleMap.clear()
                                category = binding.autoCompleteBencana.text.toString()

                                when (category) {
                                    "Bencana Alam" -> {
                                        binding.textInputLayout.setStartIconDrawable(R.drawable.ic_category_volcano)
                                    }
                                    "Infrastruktur Kurang Baik" -> {
                                        binding.textInputLayout.setStartIconDrawable(R.drawable.ic_category_danger)
                                    }
                                    "Kekayaan Alam" -> {
                                        binding.textInputLayout.setStartIconDrawable(R.drawable.ic_category_tree)
                                    }
                                    "Jalur Evakuasi" -> {
                                        binding.textInputLayout.setStartIconDrawable(R.drawable.ic_category_evacuation)
                                    }
                                    else -> {
                                        binding.textInputLayout.setStartIconDrawable(R.drawable.ic_round_grid_view_24)
                                    }
                                }

                                for (d in data) {
                                    val loc = LatLng(
                                        d.latitude.toDouble(),
                                        d.longitude.toDouble()
                                    )

                                    if (category == "Pilih Bencana" || category == "Lihat Semua" || category == "") {
                                        addMarkerMaps(d.category.id, googleMap, loc, d.id)
                                    } else {
                                        if (d.category.category == category) {
                                            addMarkerMaps(d.category.id, googleMap, loc, d.id)
                                        } else {
                                            Log.d("Category", "Null")
                                        }
                                    }
                                    googleMap.setOnMarkerClickListener(this)
                                }
                            }

                        if (category == "Pilih Bencana" || category == "Lihat Semua" || category == "") {
                            for (d in data) {
                                val loc = LatLng(
                                    d.latitude.toDouble(),
                                    d.longitude.toDouble()
                                )

                                addMarkerMaps(d.category.id, googleMap, loc, d.id)

                                googleMap.setOnMarkerClickListener(this)

                                binding.textInputLayout.setStartIconDrawable(R.drawable.ic_round_grid_view_24)
                            }
                        }
                    }
                }
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(semeru, 12.2f))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bindingSheet = BottomSheetMapsBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bindingSheet.root)

        bottomSheetDialog.show()

        val post = listPosts.single {
            it.id == marker.tag.toString().toInt()
        }

        val name = post.user.name

        val imageUrl = Constants.BASE_URL + post.image

        bindingSheet.apply {
            tvDescription.text = post.description
            tvDate.text = Converter.convertDate(post.createdAt)
            tvUser.text = name
            tvCategory.text = post.category.category
            Glide.with(requireView()).load(imageUrl).into(ivPost)

            setAddress(
                tvAddress,
                post.latitude.toDouble(),
                post.longitude.toDouble()
            )

            btnRoute.setOnClickListener {

                val latitude = "-8.201375"
                val longitude =
                    "112.9837633"
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                mapIntent.resolveActivity(requireActivity().packageManager)
                    ?.let {
                        startActivity(mapIntent)
                    }
            }
        }

        return false
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

    private fun addMarkerMaps(id: Int, googleMap: GoogleMap, loc: LatLng, id1: Int) {
        when (id) {
            1 -> {
                val markerMap = googleMap.addMarker(
                    MarkerOptions().position(loc)
                        .icon(
                            BitmapDescriptor.bitmapDescriptorFromVector(
                                requireContext(),
                                R.drawable.ic_category_volcano
                            )
                        )
                )
                markerMap?.tag = id1
            }
            2 -> {
                val markerMap = googleMap.addMarker(
                    MarkerOptions().position(loc)
                        .icon(
                            BitmapDescriptor.bitmapDescriptorFromVector(
                                requireContext(),
                                R.drawable.ic_category_danger
                            )
                        )
                )
                markerMap?.tag = id1
            }
            3 -> {
                val markerMap = googleMap.addMarker(
                    MarkerOptions().position(loc)
                        .icon(
                            BitmapDescriptor.bitmapDescriptorFromVector(
                                requireContext(),
                                R.drawable.ic_category_tree
                            )
                        )
                )
                markerMap?.tag = id1
            }
            else -> {
                val markerMap = googleMap.addMarker(
                    MarkerOptions().position(loc)
                        .icon(
                            BitmapDescriptor.bitmapDescriptorFromVector(
                                requireContext(),
                                R.drawable.ic_category_evacuation
                            )
                        )
                )
                markerMap?.tag = id1
            }
        }
    }

}