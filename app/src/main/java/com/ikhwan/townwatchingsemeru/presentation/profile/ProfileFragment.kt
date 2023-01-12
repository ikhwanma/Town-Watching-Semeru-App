package com.ikhwan.townwatchingsemeru.presentation.profile

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.*
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetPostBinding
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetProfileBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentProfileBinding
import com.ikhwan.townwatchingsemeru.domain.model.User
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private val imageIcon =
        mutableListOf(R.drawable.ic_round_grid_view_24, R.drawable.ic_red_favorite_24)
    private val textAdapter =
        mutableListOf("Laporan Saya", "Laporan Disukai")

    private var token = ""

    private var currentPhotoPath: String = ""

    private var user: User? = null

    private lateinit var dialog: ShowLoadingAlertDialog

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach { permission ->
            if (!permission.value) {
                PermissionChecker.checkPostPermission(requireContext(), requireActivity())
                val checkSelfPermission =
                    PermissionChecker.checkSelfPostPermission(requireContext())
                if (!checkSelfPermission) {
                    ShowSnackbarPermission().permissionDisabled(requireView(), requireActivity())
                }
            }
        }
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
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
            setImage(rotatedBitmap)
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

        updateAva(uriImage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        dialog = ShowLoadingAlertDialog(requireActivity())
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

        binding.apply {
            viewPager2.adapter = ProfilePagerAdapter(requireParentFragment())

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.setIcon(imageIcon[position])
                tab.text = textAdapter[position]
            }.attach()

            btnSettings.setOnClickListener(this@ProfileFragment)
            ivAddImage.setOnClickListener(this@ProfileFragment)
            imgUser.setOnClickListener(this@ProfileFragment)
        }

        viewModel.getToken().observe(viewLifecycleOwner) {
            if (it != "") {
                token = it
                getData(it)
            } else {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment2_to_loginFragment)
            }
        }

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

    private fun getData(token: String) {
        viewModel.getUser(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("ProfileFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("ProfileFragment", "Loading Profile")
                }
                is Resource.Success -> {
                    result.data!!.let { user ->
                        val imageUrl = Constants.BASE_URL + user.image

                        val sBCategory = StringBuilder("(")
                        sBCategory.append("${user.categoryUser.categoryUser})")
                        val circularProgressDrawable = CircularProgressDrawable(requireContext())
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()

                        binding.apply {
                            Glide.with(requireView()).load(imageUrl)
                                .placeholder(circularProgressDrawable).into(imgUser)
                            tvUser.text = user.name
                            tvCategory.text = sBCategory.toString()
                        }

                        this.user = user
                    }
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun updateAva(image: Uri) {
        val contentResolver = requireActivity().applicationContext.contentResolver

        val type = contentResolver.getType(image)
        val tempFile = File.createTempFile("temp-", ".jpg", null)
        val inputStream = contentResolver.openInputStream(image)

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

        viewModel.updateAva(token, imageUpload).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                    Log.d("ProfileFragment", "Loading Update Ava")
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    Toast.makeText(
                        requireContext(),
                        result.data!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    onResume()
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_settings -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bindingSheet = BottomSheetProfileBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bindingSheet.root)
                bottomSheetDialog.show()

                bindingSheet.apply {
                    btnLogout.setOnClickListener {
                        ShowActionAlertDialog(
                            context = requireContext(),
                            title = "Logout",
                            message = "Apakah Anda yakin ingin logout?",
                            positiveButtonAction = {
                                viewModel.setId(0)
                                viewModel.setToken("")
                                Navigation.findNavController(requireView())
                                    .navigate(R.id.action_profileFragment2_to_loginFragment)
                                bottomSheetDialog.dismiss()
                            }
                        ).invoke()

                    }
                    btnEditPassword.setOnClickListener {
                        bottomSheetDialog.dismiss()
                        val mBundle = bundleOf(Constants.EXTRA_TOKEN to token)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_profileFragment_to_editPasswordFragment, mBundle)
                    }
                    btnEditProfile.setOnClickListener {
                        bottomSheetDialog.dismiss()
                        val name = user!!.name
                        val email = user!!.email
                        val categoryUserId = user!!.categoryUser.id
                        val mBundle = bundleOf(
                            Constants.EXTRA_NAME to name,
                            Constants.EXTRA_CATEGORY to categoryUserId,
                            Constants.EXTRA_EMAIL to email,
                            Constants.EXTRA_TOKEN to token
                        )
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_profileFragment_to_updateProfileFragment, mBundle)
                    }
                }
            }
            R.id.iv_add_image -> {
                showBottomSheet()
            }
            R.id.img_user -> {
                showBottomSheet()
            }
        }
    }

    private fun showBottomSheet() {
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

}