package com.ikhwan.townwatchingsemeru.presentation.post

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.*
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


@Suppress("DEPRECATION")
class PostFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private var image: Uri? = null
    private val listCategory = mutableListOf<Category>()
    private val listCategoryName = mutableListOf<String>()
    private var token = ""
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentPhotoPath: String = ""
    private var currentLocation: LatLng? = null
    private var level = ""
    private var cekBencana = false

    private val viewModel: PostViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private lateinit var dialog: ShowLoadingAlertDialog

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach { permission ->
            if (permission.value) {
                getCurrentLocation()
            } else {
                PermissionChecker.checkPostPermission(requireContext(), requireActivity())

                val checkSelfPermission = PermissionChecker.checkSelfPostPermission(requireContext())
                if (!checkSelfPermission) {
                    ShowSnackbarPermission().permissionDisabled(requireView(), requireActivity())
                }
            }
        }
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null){
                val bMap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, result)

                val bitmap = if (bMap.height >= bMap.width) {
                    BitmapResize.getResizedBitmap(
                        bMap,
                        1080,
                        1920
                    )
                } else {
                    BitmapResize.getResizedBitmap(
                        bMap,
                        1920,
                        1080
                    )
                }
                val ei = ExifInterface(PathUtil.getPath(requireContext(), result!!)!!)
                val orientation: Int = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                val rotatedBitmap = BitmapRotator.getRotatedBitmap(orientation, bitmap!!)
                binding.ivPost.setImageBitmap(rotatedBitmap)
                setImage(rotatedBitmap)
            }
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bMap = BitmapFactory.decodeFile(currentPhotoPath)
                val bitmap = if (bMap.height >= bMap.width) {
                    BitmapResize.getResizedBitmap(
                        bMap,
                        1080,
                        1920
                    )
                } else {
                    BitmapResize.getResizedBitmap(
                        bMap,
                        1920,
                        1080
                    )
                }

                val ei = ExifInterface(currentPhotoPath)
                val orientation: Int = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )

                val rotatedBitmap = BitmapRotator.getRotatedBitmap(orientation, bitmap!!)

                binding.ivPost.setImageBitmap(rotatedBitmap)
                setImage(rotatedBitmap)
            }
        }

    private fun setImage(rotatedBitmap: Bitmap) {
        val file = File(requireContext().cacheDir, "semeru-")
        file.run {
            delete()
            createNewFile()
        }
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

        val bytearray = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.run {
            fileOutputStream.write(bytearray)
            fileOutputStream.flush()
            fileOutputStream.close()
            close()
        }
        val uriImage = file.toUri()

        image = uriImage
    }

    private fun observeCategory() {
        viewModel.getCategory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Category UserDto")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("UriImage", image)
        outState.putString("ImagePath", currentPhotoPath)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (!savedInstanceState?.getString("ImagePath").isNullOrEmpty()) {
            this.currentPhotoPath = savedInstanceState?.getString("ImagePath")!!
        }
        image = savedInstanceState?.getParcelable("UriImage")
        binding.ivPost.setImageURI(image)

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
        dialog = ShowLoadingAlertDialog(requireActivity())
        viewModel.getToken().observe(viewLifecycleOwner) {
            if (it == "") {
                goToLoginPage()
            } else {
                this.token = it
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        observeCategory()

        initAdapter()

        requestPermission()

        binding.apply {
            ivPost.setOnClickListener(this@PostFragment)
            btnPost.setOnClickListener(this@PostFragment)
        }

    }

    private fun requestPermission(){
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
        val checkSelfPermission = PermissionChecker.checkSelfPostPermission(requireContext())

        if (!checkSelfPermission) {
            ShowSnackbarPermission().permissionDisabled(requireView(), requireActivity())
        }else{
            getCurrentLocation()
        }
    }

    private fun initAdapter() {
        val listLevel = mutableListOf("Ringan", "Sedang", "Berat")

        val adapterLevel = ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listLevel)
        val adapterCategory =
            ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listCategoryName)

        binding.apply {
            autoCompleteLevel.setAdapter(adapterLevel)
            autoCompleteBencana.setAdapter(adapterCategory)
            autoCompleteBencana.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, _, _ ->
                    val category = autoCompleteBencana.text.toString()
                    if (category == listCategory[2].category || category == listCategory[3].category){
                        textInputLevel.visibility = View.GONE
                        tvPilihLevel.visibility = View.GONE
                    }else{
                        textInputLevel.visibility = View.VISIBLE
                        tvPilihLevel.visibility = View.VISIBLE
                        cekBencana = true
                    }
                }
        }


    }


    private fun goToLoginPage() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_postFragment_to_loginFragment)
        Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT)
            .show()
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
            }else {
                ShowSnackbarPermission().locationDisabled(requireView(),requireActivity())
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

                            if (image != null) {
                                image = null
                                currentPhotoPath = ""
                            }

                            val fileName = "photo"
                            val storageDirectory =
                                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                            try {
                                val imageFile =
                                    File.createTempFile(fileName, ".jpg", storageDirectory)

                                currentPhotoPath = imageFile.absolutePath

                                val imageUri = FileProvider.getUriForFile(
                                    requireContext(),
                                    "com.ikhwan.townwatchingsemeru",
                                    imageFile
                                )

                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                                cameraResult.launch(intent)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

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
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        101
                    )
                } else {
                    binding.apply {
                        if (currentLocation == null) {
                            ShowSnackbarPermission().locationDisabled(requireView(),requireActivity())
                            return
                        }

                        val description = etDescription.text.toString()
                        val latitude = currentLocation!!.latitude.toString()
                        val longitude = currentLocation!!.longitude.toString()
                        val level = if(cekBencana){
                            autoCompleteLevel.text.toString()
                        }else{
                            "Ringan"
                        }
                        var category = ""

                        for (d in listCategory) {
                            if (d.category == autoCompleteBencana.text.toString()) {
                                category = d.id.toString()
                            }
                        }

                        val postValidator =
                            Validator.postValidator(
                                description = description,
                                image = image,
                                category = category,
                                level = level
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
                                level = level
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun addProduct(
        description: String,
        latitude: String,
        longitude: String,
        category: String,
        level: String
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
        val statusUpload = "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())

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
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
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