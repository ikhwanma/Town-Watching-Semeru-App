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
import com.ikhwan.townwatchingsemeru.common.utils.Converter
import com.ikhwan.townwatchingsemeru.databinding.BottomSheetHomeCategoryBinding
import com.ikhwan.townwatchingsemeru.databinding.FragmentHomeBinding
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser
import com.ikhwan.townwatchingsemeru.domain.model.Post
import kotlinx.coroutines.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var userId: Int = 0
    private var token: String = ""
    private var categoryUser: List<CategoryUser> = emptyList()
    private var listCategoryName = mutableListOf<String>()
    private var listCategory = mutableListOf<Category>()

    private var categoryId: Int? = null
    private var category: String? = null
    private var level: String? = null
    private var status: Int? = null

    private fun observeGetId() = lifecycleScope.launchWhenStarted {
        viewModel.getId().observe(viewLifecycleOwner) { id ->
            userId = id
        }
    }

    private fun observeGetToken() = lifecycleScope.launchWhenStarted {
        viewModel.getToken().observe(viewLifecycleOwner) {
            token = it
        }
        viewModel.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNjcxODcxNzA1LCJleHAiOjE2NzQ0NjM3MDV9.6CXPCMzn_HYPwg4ZPbmLtL-I3mLUAE8nrymAAwTJBwc")
        viewModel.setId(1)
    }

    private fun observeListPosts(
        categoryId: Int? = null,
        level: String? = null,
        status: Int? = null
    ) = lifecycleScope.launchWhenStarted {
        viewModel.getAllPosts(categoryId, level, status).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Category listAllPost")
                    observeCategoryUser()
                }
                is Resource.Success -> {
                    val data = result.data!!
                    setAdapter(data)
                }
            }
        }
    }

    private fun observeCategoryUser() = lifecycleScope.launchWhenStarted {
        viewModel.getCategoryUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Category User")
                }
                is Resource.Success -> {
                    categoryUser = result.data!!
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            val adapter = PostAdapter(userId, requireContext(), { tvAddress, lat, lng ->
                setAddress(tvAddress, lat, lng)
            }, { tvUser, idCategoryUser, name ->
                if (categoryUser.isNotEmpty()) {
                    val categoryUserFiltered = categoryUser.filter {
                        it.id == idCategoryUser
                    }
                    val txtName = "$name (${categoryUserFiltered[0].categoryUser})"
                    tvUser.text = txtName
                }
            }, { id ->
                if (userId == 0) {
                    Toast.makeText(requireContext(), "Belum Login", Toast.LENGTH_SHORT).show()
                } else {
                    val mBundle = bundleOf(Constants.EXTRA_ID to id, Constants.EXTRA_TOKEN to token)
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_commentFragment, mBundle)
                }
            }, { post, btnLike, tvLike ->
                if (userId == 0) {
                    Toast.makeText(requireContext(), "Belum Login", Toast.LENGTH_SHORT).show()
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

    private fun setAddress(tvAddress: TextView, lat: Double, lng: Double) {
        Thread(Runnable {
            val address =
                Converter.convertAddress(requireContext(), lat, lng)
            tvAddress.post(Runnable {
                tvAddress.text = address
            })
        }).start()
    }

    private fun likeChecker(
        post: Post,
        btnLike: ImageView,
        tvLike: TextView
    ) {
        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim)

        btnLike.startAnimation(animation)

        Toast.makeText(requireContext(), userId.toString(), Toast.LENGTH_SHORT).show()

        var likeSum = post.like.size

        viewModel.addLike(token, post.id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Log.d("HomeFragment", result.message!!)
                }
                is Resource.Loading -> {
                    Log.d("HomeFragment", "Loading Category User")
                }
                is Resource.Success -> {
                    val message = result.data!!.message

                    if (message == "Berhasil menghapus like") {
                        likeSum--
                        val sBLike =
                            StringBuilder(likeSum.toString())

                        sBLike.append(" Menyukai")
                        tvLike.text = sBLike.toString()
                        btnLike.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)

                    } else {
                        likeSum++

                        val sBLike =
                            StringBuilder(likeSum.toString())

                        sBLike.append(" Menyukai")
                        tvLike.text = sBLike.toString()

                        btnLike.setBackgroundResource(R.drawable.ic_red_favorite_24)

                    }

                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                    observeListPosts()
                    bottomSheetDialog.dismiss()
                }
                val listImage = listOf(
                    R.drawable.ic_category_volcano,
                    R.drawable.ic_category_danger,
                    R.drawable.ic_category_tree,
                    R.drawable.ic_category_evacuation
                )


                val adapter = CategoryAdapter(listImage) { it1 ->
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
                    observeListPosts()
                    bottomSheetDialog.dismiss()
                }

                val listKerusakan = mutableListOf("Berat", "Sedang", "Ringan")
                val listImage = mutableListOf(
                    R.drawable.ic_red_brightness_1_24,
                    R.drawable.ic_yellow_brightness_1_24,
                    R.drawable.ic_green_brightness_1_24
                )

                val adapter = CategoryAdapter(listImage) { it1 ->
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
                    observeListPosts()
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