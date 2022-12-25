package com.ikhwan.townwatchingsemeru.presentation.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.PermissionChecker
import com.ikhwan.townwatchingsemeru.common.utils.Validator
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetPostBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentPostBinding
import com.ikhwan.townwatchingsemeru.domain.model.Category
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*


class PostFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private var image: Uri? = null
    private val listCategory = mutableListOf<Category>()
    private val listCategoryName = mutableListOf<String>()
    private var token = ""
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null

    private val viewModel: PostViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach { permission ->
            if (permission.value) {
                getCurrentLocation()
            } else {
                PermissionChecker.checkPostPermission(requireContext(), requireActivity())
            }
        }
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.ivPost.setImageURI(result)
            image = result!!
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result?.data?.extras?.get("data") as Bitmap

                val file = File(requireContext().cacheDir, "semeru-")
                file.createNewFile()

                val bos = ByteArrayOutputStream()

                val bitmapdata = bos.toByteArray();

                val path = MediaStore.Images.Media.insertImage(
                    requireActivity().contentResolver,
                    bitmap,
                    "semeru",
                    null
                )
                var fos : FileOutputStream? = null
                try {
                    fos = FileOutputStream(file)
                }catch (e: FileNotFoundException) {
                    e.printStackTrace();
                }
                try {
                    fos?.write(bitmapdata)
                    fos?.flush()
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val uriImage = file.toUri()

                Toast.makeText(requireContext(), uriImage.toString(), Toast.LENGTH_SHORT).show()

                binding.ivPost.setImageURI(Uri.parse(path))
                image = uriImage
            }
        }

    private fun observeCategory() {
        viewModel.getCategory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Category User")
                }
                is Resource.Success -> {
                    val data = result.data!!

                    for (d in data) {
                        listCategoryName.add(d.category)
                    }
                    listCategory.addAll(data)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getToken().observe(viewLifecycleOwner) {
            if (it == "") {
                Toast.makeText(requireContext(), "Belum Login", Toast.LENGTH_SHORT).show()
            } else {
                this.token = it
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        observeCategory()

        val listLevel = mutableListOf("Ringan", "Sedang", "Berat")
        val listStatus = mutableListOf("Aktif", "Tidak Aktif")

        val adapterLevel = ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listLevel)
        val adapterStatus = ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listStatus)
        val adapterCategory =
            ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listCategoryName)

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )

        binding.apply {
            autoCompleteLevel.setAdapter(adapterLevel)
            autoCompleteStatus.setAdapter(adapterStatus)
            autoCompleteBencana.setAdapter(adapterCategory)

            ivPost.setOnClickListener(this@PostFragment)
            btnPost.setOnClickListener(this@PostFragment)
        }

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
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_post -> {
                val checkSelfPermission =
                    PermissionChecker.checkSelfPostPermission(requireContext())

                if (!checkSelfPermission) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        101
                    )
                } else {
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    val bindingSheet = BottomSheetPostBinding.inflate(layoutInflater)

                    bottomSheetDialog.setContentView(bindingSheet.root)
                    bottomSheetDialog.show()

                    bindingSheet.apply {
                        btnCamera.setOnClickListener {
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraResult.launch(cameraIntent)
                            bottomSheetDialog.dismiss()
                        }

                        btnGallery.setOnClickListener {
                            galleryResult.launch("image/*")
                            bottomSheetDialog.dismiss()
                        }
                    }
                }
            }
            R.id.btn_post -> {
                val checkSelfPermission =
                    PermissionChecker.checkSelfPostPermission(requireContext())

                if (!checkSelfPermission) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        101
                    )
                } else {
                    binding.apply {
                        val description = etDescription.text.toString()
                        val latitude = currentLocation!!.latitude.toString()
                        val longitude = currentLocation!!.longitude.toString()
                        var category = ""
                        for (d in listCategory) {
                            if (d.category == autoCompleteBencana.text.toString()) {
                                category = d.id.toString()
                            }
                        }
                        val level = autoCompleteLevel.text.toString()
                        val txtStatus = autoCompleteLevel.text.toString()
                        val status = txtStatus == "Aktif"

                        val postValidator =
                            Validator.postValidator(
                                description = description,
                                image = image,
                                category = category,
                                level = level,
                                txtStatus = txtStatus
                            )

                        if (postValidator) {
                            Toast.makeText(
                                requireContext(),
                                "Field tidak boleh kosong",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            addProduct(
                                description = description,
                                latitude = latitude,
                                longitude = longitude,
                                category = category,
                                level = level,
                                status = status
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addProduct(
        description: String,
        latitude: String,
        longitude: String,
        category: String,
        level: String,
        status: Boolean
    ) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val type = contentResolver.getType(image!!)
        val tempFile = File.createTempFile("temp-", null, null)
        val inputStream = contentResolver.openInputStream(image!!)

        tempFile.outputStream().use {
            inputStream?.copyTo(it)
        }

        val typeNotNull = if (type.isNullOrEmpty()) {
            "image/jpeg"
        } else {
            type
        }

        val requestBody: RequestBody = tempFile.asRequestBody(typeNotNull.toMediaType())

        val imageUpload =
            MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
        val latitudeUpload =
            latitude.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val longitudeUpload =
            longitude.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val descriptionUpload =
            description.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val categoryUpload = category.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val levelUpload = level.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val statusUpload = if (status) {
            "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        } else {
            "0".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        viewModel.addPost(
            auth = token,
            description = descriptionUpload,
            latitude = latitudeUpload,
            longitude = longitudeUpload,
            category = categoryUpload,
            level = levelUpload,
            status = statusUpload,
            image = imageUpload
        ).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Berhasil Menambahkan Laporan",
                        Toast.LENGTH_SHORT
                    ).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_postFragment_to_homeFragment)
                }
            }
        }
    }

}