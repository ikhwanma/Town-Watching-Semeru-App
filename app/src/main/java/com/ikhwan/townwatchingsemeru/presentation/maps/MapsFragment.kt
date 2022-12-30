package com.ikhwan.townwatchingsemeru.presentation.maps

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.scale
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
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
    private var currentLocation: LatLng? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach { map ->
            if (map.value) {

                getCurrentLocation()
            } else {
                PermissionChecker.checkMapsPermission(requireContext(), requireActivity())
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
                    for (d in data) {
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


    private fun init() {
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
        val checkSelfPermission = PermissionChecker.checkSelfPostPermission(requireContext())

        if (!checkSelfPermission) {
            return
        }

        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {
                val currentLocation = LatLng(location.latitude, location.longitude)

                this.currentLocation = currentLocation
            }
        }

        val semeru = LatLng(-8.2061557, 112.9773703)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            googleMap.isMyLocationEnabled = true

            viewModel.getAllPosts().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        Log.d("MapsFragment", "Loading Post")
                    }
                    is Resource.Success -> {
                        val data = result.data!!

                        listPosts = data

                        var category = binding.autoCompleteBencana.text.toString()

                        binding.autoCompleteBencana.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, _, _ ->
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
                                        addMarkerMaps(
                                            googleMap,
                                            loc,
                                            d.id,
                                            d.category.image
                                        )
                                    } else {
                                        if (d.category.category == category) {
                                            addMarkerMaps(
                                                googleMap,
                                                loc,
                                                d.id,
                                                d.category.image
                                            )
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

                                addMarkerMaps(googleMap, loc, d.id, d.category.image)

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

        val nearestLatLng = getNearestPoint()

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

            if (post.category.id == 4 || post.category.id == 3) {
                val txtBtn = "Rute ke tempat ini"
                btnRoute.text = txtBtn
                btnRoute.setOnClickListener {
                    val gmmIntentUri =
                        Uri.parse("google.navigation:q=${post.latitude},${post.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    mapIntent.resolveActivity(requireActivity().packageManager)
                        ?.let {
                            startActivity(mapIntent)
                        }
                }
            } else {
                btnRoute.setOnClickListener {
                    if (nearestLatLng != LatLng(0.0, 0.0)) {
                        val latitude = nearestLatLng.latitude.toString()
                        val longitude = nearestLatLng.longitude.toString()
                        val gmmIntentUri =
                            Uri.parse("google.navigation:q=$latitude,$longitude")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        mapIntent.resolveActivity(requireActivity().packageManager)
                            ?.let {
                                startActivity(mapIntent)
                            }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Tidak ada posko evakuasi yang aktif",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        return false
    }

    private fun getNearestPoint(): LatLng {
        val myLatitude = currentLocation!!.latitude
        val myLongitude = currentLocation!!.longitude
        val posko = listPosts.filter { it.category.id == 4 }
        val poskoActive = posko.filter { it.status }
        val listDistance = HashMap<LatLng, Double>()
        val listDistanceWithIndex = HashMap<Int, LatLng>()
        var i = 0

        val startPoint = Location("LocationA")
        startPoint.latitude = myLatitude
        startPoint.longitude = myLongitude

        for (d in poskoActive) {

            val endPoint = Location("LocationB")
            endPoint.latitude = d.latitude.toDouble()
            endPoint.longitude = d.longitude.toDouble()

            val distance = startPoint.distanceTo(endPoint).toDouble()

            listDistance[LatLng(d.latitude.toDouble(), d.longitude.toDouble())] = distance

        }

        val listDistanceSorted = listDistance.toList().sortedBy { (_, value) -> value }.toMap()

        for (d in listDistanceSorted) {
            listDistanceWithIndex[i] = d.key
            i++
        }


        return if (listDistanceWithIndex.isNotEmpty()) {
            listDistanceWithIndex[0]!!
        } else {
            LatLng(0.0, 0.0)
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

    private fun addMarkerMaps(googleMap: GoogleMap, loc: LatLng, id1: Int, image: String) {
        val imageUrl = Constants.BASE_URL + image

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

}