package com.ikhwan.townwatchingsemeru.presentation.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.scale
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.ikhwan.townwatchingsemeru.common.utils.*
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetMapsBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentMapsBinding
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetInfoMapsBinding
import com.ikhwan.townwatchingsemeru.domain.model.Category

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapsViewModel by hiltNavGraphViewModels(R.id.nav_main)
    private var listPosts: List<Post> = emptyList()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var currentLocation: LatLng? = null

    private val listCategoryInfo = mutableListOf<Category>()

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

                    listCategoryInfo.addAll(data)
                    val listCategory = mutableListOf("Lihat Semua")
                    for (d in data) {
                        listCategory.add(d.category)
                    }

                    Log.d("ListCategoryInfo", listCategoryInfo.toString())
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
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        binding.btnInfo.setOnClickListener(this)
        observeCategory()

        getCurrentLocation()
    }

    override fun onPause() {
        super.onPause()
        binding.autoCompleteBencana.text = null
    }

    override fun onResume() {
        super.onResume()
        getCurrentLocation()
    }

    private fun init() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getCurrentLocation() {
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
        } else {
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
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(semeru, 12.2f))

                viewModel.getAllPosts().observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Loading -> {
                            Log.d("MapsFragment", "Loading Post")
                        }
                        is Resource.Success -> {
                            val data = result.data!!

                            listPosts = data

                            var category = binding.autoCompleteBencana.text.toString()
                            binding.textInputLayout.setStartIconDrawable(R.drawable.ic_round_grid_view_24)
                            binding.autoCompleteBencana.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, _, _ ->
                                    googleMap.clear()
                                    category = binding.autoCompleteBencana.text.toString()

                                    for (d in data) {
                                        val loc = LatLng(
                                            d.latitude.toDouble(),
                                            d.longitude.toDouble()
                                        )
                                        val imageUrl = Constants.BASE_URL + d.category.image

                                        if (category == "Pilih Bencana" || category == "Lihat Semua" || category == "") {
                                            addMarkerUrl(imageUrl, googleMap, loc, d.id)
                                        } else {
                                            if (d.category.category == category) {
                                                addMarkerUrl(imageUrl, googleMap, loc, d.id)
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
                                    val imageUrl = Constants.BASE_URL + d.category.image

                                    addMarkerUrl(imageUrl, googleMap, loc, d.id)

                                    googleMap.setOnMarkerClickListener(this)

                                    binding.textInputLayout.setStartIconDrawable(R.drawable.ic_round_grid_view_24)
                                }
                            }
                        }
                    }
                }
            }
        }


    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_info -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bindingSheet = BottomSheetInfoMapsBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bindingSheet.root)
                bottomSheetDialog.show()

                bindingSheet.apply {
                    val adapter = InfoAdapter() {
                        val mBundle = when (it) {
                            "Erupsi" -> {
                                bundleOf(
                                    Constants.EXTRA_ID to 1,
                                    Constants.EXTRA_CATEGORY to it
                                )
                            }
                            "Gempa Bumi" -> {
                                bundleOf(
                                    Constants.EXTRA_ID to 2,
                                    Constants.EXTRA_CATEGORY to it
                                )
                            }
                            "Tanah Longsor" -> {
                                bundleOf(
                                    Constants.EXTRA_ID to 3,
                                    Constants.EXTRA_CATEGORY to it
                                )
                            }
                            "Banjir" -> {
                                bundleOf(
                                    Constants.EXTRA_ID to 4,
                                    Constants.EXTRA_CATEGORY to it
                                )
                            }
                            "Angin Kencang" -> {
                                bundleOf(
                                    Constants.EXTRA_ID to 5,
                                    Constants.EXTRA_CATEGORY to it
                                )
                            }
                            else -> {
                                bundleOf(
                                    Constants.EXTRA_ID to 0,
                                    Constants.EXTRA_CATEGORY to ""
                                )
                            }
                        }
                        if(it != "Titik Kumpul"){
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_mapsFragment_to_bukuSakuDetailFragment, mBundle)
                        }else{
                            Toast.makeText(requireContext(), "Cari titik kumpul terdekat saat bencana terjadi!", Toast.LENGTH_SHORT).show()
                        }

                        bottomSheetDialog.dismiss()
                    }
                    adapter.submitData(listCategoryInfo)

                    rvCategory.layoutManager = LinearLayoutManager(requireContext())
                    rvCategory.adapter = adapter
                }
            }
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
            tvDate.text = Converter.convertDate(post.updatedAt)
            tvUser.text = name
            tvCategory.text = post.category.category
            tvAddress.text = post.address
            Glide.with(requireView()).load(imageUrl).into(ivPost)


            var cardStatusDrawable = cvStatus.background
            var textStatus = ""
            val status = post.status


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

            if (post.category.id == 4 && status) {
                textStatus = "Laporan Aktif"
                DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#00BDAA"))
            } else if (post.category.id == 4 && !status) {
                textStatus = "Laporan Tidak Aktif"
                DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#CF0A0A"))
            } else {
                if (status) {
                    textStatus = "Laporan Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#CF0A0A"))
                } else {
                    textStatus = "Laporan Tidak Aktif"
                    DrawableCompat.setTint(cardStatusDrawable, Color.parseColor("#00BDAA"))
                }
            }

            tvStatus.text = textStatus

            if (post.category.id == 4 || post.category.id == 3) {
                val txtBtn = "Rute ke tempat ini"
                btnRoute.text = txtBtn
                btnRoute.setOnClickListener {
                    init()
                    if (!isLocationEnable(bottomSheetDialog)) return@setOnClickListener
                    if (currentLocation == null) {
                        Toast.makeText(
                            requireContext(),
                            "Lokasi belum siap, coba beberapa saat lagi",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
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
                    init()
                    if (!isLocationEnable(bottomSheetDialog)) return@setOnClickListener
                    if (currentLocation == null) {
                        Toast.makeText(
                            requireContext(),
                            "Lokasi belum siap, coba beberapa saat lagi",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    isLocationEnable(bottomSheetDialog)
                    val nearestLatLng = getNearestPoint()
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

    private fun isLocationEnable(bottomSheetDialog: BottomSheetDialog): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!providerEnable) {
            bottomSheetDialog.dismiss()
            ShowActionAlertDialog(
                context = requireContext(),
                title = "Lokasi Tidak Diaktifkan",
                message = "Aplikasi membutuhkan akses Lokasi, buka setting untuk mengaktifkan lokasi sekarang?",
                positiveButtonText = "Buka Setting",
                negativeButtonText = "Nanti",
                positiveButtonAction = {
                    val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    requireActivity().startActivity(i)
                }
            ).invoke()
            return false
        }
        return true
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
}