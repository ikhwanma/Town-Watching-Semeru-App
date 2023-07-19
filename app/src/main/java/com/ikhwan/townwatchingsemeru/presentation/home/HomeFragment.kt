package com.ikhwan.townwatchingsemeru.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetHomeCategoryBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentHomeBinding
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.Post
import kotlinx.coroutines.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var userId: Int = 0
    private var token: String = ""
    private var listCategoryName = mutableListOf<String>()
    private var listCategory = mutableListOf<Category>()

    private var categoryId: Int? = null
    private var category: String? = null
    private var level: String? = null
    private var status: Int? = null

    private fun observeGetId() {
        viewModel.getId().observe(viewLifecycleOwner) { id ->
            if (id != 0) {
                userId = id
            }
        }
    }

    private fun observeGetToken() {

        viewModel.getToken().observe(viewLifecycleOwner) {
            if (it != "") {
                token = it
            }

        }
    }

    private fun observeListPosts(
        categoryId: Int? = null,
        level: String? = null,
        status: Int? = null
    ) {
        viewModel.observeListPosts(categoryId, level, status).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    result.data?.let {
                        if (result.data.isEmpty()) {
                            binding.tvAlertEmptyPost.visibility = View.VISIBLE
                            setAdapter(emptyList())
                        } else {
                            binding.tvAlertEmptyPost.visibility = View.GONE
                            setAdapter(it)
                        }
                    }
                }
            }
        }
    }

    private fun observeCategory() = lifecycleScope.launchWhenStarted {
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
                    listCategory.clear()
                    listCategoryName.clear()
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.show()
        observeGetId()
        observeListPosts(null, null, null)
        observeGetToken()
        observeCategory()

        binding.apply {
            btnFilterCategory.setOnClickListener(this@HomeFragment)
            btnFilterKerusakan.setOnClickListener(this@HomeFragment)
            btnFilterStatus.setOnClickListener(this@HomeFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(posts: List<Post>) {
        binding.apply {

            progressCircular.visibility = View.GONE
            rvPost.visibility = View.VISIBLE

            val adapter = PostAdapter(userId, requireContext(), { id ->
                if (userId == 0) {
                    goToLoginPage()
                } else {
                    val mBundle = bundleOf(Constants.EXTRA_ID to id, Constants.EXTRA_TOKEN to token)
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_commentFragment, mBundle)
                }
            }, { post, btnLike, tvLike ->
                if (userId == 0) {
                    goToLoginPage()
                } else {
                    likeChecker(post, btnLike, tvLike)
                }
            })
            adapter.submitData(posts)
            adapter.notifyDataSetChanged()

            rvPost.adapter = adapter
            rvPost.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun goToLoginPage() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_loginFragment)
        Toast.makeText(requireContext(), "Login terlebih dahulu", Toast.LENGTH_SHORT)
            .show()
    }

    private fun getLike(id: Int, tvLike: TextView) {
        viewModel.getPostLike(id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Get Like")
                }
                is Resource.Success -> {
                    result.data.let { like ->
                        val sBLike =
                            StringBuilder(like!!.size.toString())


                        sBLike.append(" Menyimpan Laporan")
                        tvLike.text = sBLike.toString()
                    }
                }
            }
        }
    }

    private fun likeChecker(
        post: Post,
        btnLike: ImageView,
        tvLike: TextView
    ) {
        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim)

        btnLike.startAnimation(animation)

        viewModel.addLike(token, post.id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Add Like")
                }
                is Resource.Success -> {
                    val message = result.data!!.message

                    getLike(post.id, tvLike)

                    if (message == "Berhasil menghapus like") {
                        btnLike.setBackgroundResource(R.drawable.baseline_bookmark_border_24)
                        Toast.makeText(
                            requireContext(),
                            "Berhasil membatalkan simpan laporan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        btnLike.setBackgroundResource(R.drawable.baseline_bookmark_24)
                        Toast.makeText(
                            requireContext(),
                            "Berhasil menyimpan laporan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_filter_category -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bindingSheet = BottomSheetHomeCategoryBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bindingSheet.root)
                bottomSheetDialog.show()

                bindingSheet.btnDeleteCategory.setOnClickListener {
                    categoryId = null
                    category = null
                    setFilterCategory(null)
                    observeListPosts(categoryId, level, status)
                    bottomSheetDialog.dismiss()
                }
                val listImage = mutableListOf<String>()

                for (d in listCategory) {
                    listImage.add(d.image)
                }

                val adapter = CategoryAdapter(listImageUrl = listImage) { it1 ->
                    for (d in listCategory) {
                        if (d.category == it1) {
                            categoryId = d.id
                            break
                        }
                    }
                    category = it1
                    setFilterCategory(category)
                    observeListPosts(categoryId, level, status)
                    bottomSheetDialog.dismiss()
                }
                adapter.submitData(listCategoryName)
                val layoutManager = LinearLayoutManager(requireContext())
                bindingSheet.apply {
                    rvCategory.adapter = adapter
                    rvCategory.layoutManager = layoutManager
                }
            }
            R.id.btn_filter_kerusakan -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bindingSheet = BottomSheetHomeCategoryBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bindingSheet.root)
                bottomSheetDialog.show()
                bindingSheet.btnDeleteCategory.setOnClickListener {
                    level = null
                    setFilterLevel(null)
                    observeListPosts(categoryId, level, status)
                    bottomSheetDialog.dismiss()
                }

                val listKerusakan = mutableListOf("Berat", "Sedang", "Ringan")
                val listImage = mutableListOf(
                    R.drawable.ic_red_brightness_1_24,
                    R.drawable.ic_yellow_brightness_1_24,
                    R.drawable.ic_green_brightness_1_24
                )

                val adapter = CategoryAdapter(listImage = listImage) { it1 ->
                    level = it1
                    setFilterLevel(level)
                    observeListPosts(categoryId, level, status)
                    bottomSheetDialog.dismiss()
                }
                adapter.submitData(listKerusakan)
                val layoutManager = LinearLayoutManager(requireContext())
                bindingSheet.apply {
                    rvCategory.adapter = adapter
                    rvCategory.layoutManager = layoutManager
                }
            }
            R.id.btn_filter_status -> {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bindingSheet = BottomSheetHomeCategoryBinding.inflate(layoutInflater)
                bottomSheetDialog.setContentView(bindingSheet.root)
                bottomSheetDialog.show()
                bindingSheet.btnDeleteCategory.setOnClickListener {
                    status = null
                    setFilterStatus(null)
                    observeListPosts(categoryId, level, status)
                    bottomSheetDialog.dismiss()
                }
                val listStatus = mutableListOf("Aktif", "Tidak Aktif")
                val listImage = mutableListOf(
                    R.drawable.ic_red_brightness_1_24,
                    R.drawable.ic_green_brightness_1_24
                )
                val adapter = CategoryAdapter(listImage) { it1 ->
                    status = if (it1 == "Aktif") {
                        1
                    } else {
                        0
                    }
                    setFilterStatus(status)
                    observeListPosts(categoryId, level, status)
                    bottomSheetDialog.dismiss()
                }
                adapter.submitData(listStatus)
                val layoutManager = LinearLayoutManager(requireContext())
                bindingSheet.apply {
                    rvCategory.adapter = adapter
                    rvCategory.layoutManager = layoutManager
                }
            }
        }
    }

    private fun setFilterCategory(category: String? = null) {
        binding.apply {
            if (category != null) {
                tvFilterCategory.text = category
            } else {
                val txtCategory = "Filter Kategori"
                tvFilterCategory.text = txtCategory
            }
        }
    }

    private fun setFilterLevel(level: String? = null) {
        binding.apply {
            if (level != null) {
                tvFilterKerusakan.text = level
            } else {
                val txtLevel = "Kerusakan"
                tvFilterKerusakan.text = txtLevel
            }
        }
    }

    private fun setFilterStatus(status: Int? = null) {
        binding.apply {
            if (status != null) {
                tvFilterStatus.text = if (status == 1) {
                    "Aktif"
                } else {
                    "Tidak Aktif"
                }
            } else {
                val txtStatus = "Status"
                tvFilterStatus.text = txtStatus
            }
        }
    }


}