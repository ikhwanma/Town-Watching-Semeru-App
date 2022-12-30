package com.ikhwan.townwatchingsemeru.presentation.update_post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.ikhwan.townwatchingsemeru.R
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.common.utils.ShowLoadingAlertDialog
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.updatepost.UpdatePostBody
import com.ikhwan.townwatchingsemeru.databinding.FragmentUpdatePostBinding
import com.ikhwan.townwatchingsemeru.domain.model.Category


class UpdatePostFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentUpdatePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpdatePostViewModel by hiltNavGraphViewModels(R.id.nav_main)

    private var imageUrl = ""
    private var token = ""
    private var description = ""
    private var idPost = 0
    private var categoryId = 0
    private var level = ""
    private var status: Boolean? = null

    private val listCategories = mutableListOf<Category>()

    private lateinit var dialog: ShowLoadingAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = ShowLoadingAlertDialog(requireActivity())
        idPost = arguments?.getInt(Constants.EXTRA_ID)!!
        categoryId = arguments?.getInt(Constants.EXTRA_CATEGORY)!!
        level = arguments?.getString(Constants.EXTRA_LEVEL)!!
        token = arguments?.getString(Constants.EXTRA_TOKEN)!!
        imageUrl = arguments?.getString(Constants.EXTRA_IMAGE)!!
        description = arguments?.getString(Constants.EXTRA_DESCRIPTION)!!
        status = arguments?.getBoolean(Constants.EXTRA_STATUS)!!

        observeCategory()
        initView()

        binding.btnUpdatePost.setOnClickListener(this)
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

                    val listCategory = mutableListOf<Category>()
                    val listCategoryName = mutableListOf<String>()

                    for (d in data) {
                        listCategoryName.add(d.category)
                    }
                    listCategory.addAll(data)
                    listCategories.addAll(data)

                    initAdapter(listCategory, listCategoryName)
                }
            }
        }
    }

    private fun initAdapter(
        listCategory: MutableList<Category>,
        listCategoryName: MutableList<String>
    ) {
        val listLevel = mutableListOf("Ringan", "Sedang", "Berat")
        val listStatus = mutableListOf("Aktif", "Tidak Aktif")
        var positionLevel = 0
        val positionStatus = if (status!!) {
            0
        } else {
            1
        }
        var positionCategory = 0

        for ((i, d) in listLevel.withIndex()) {
            if (d == level) positionLevel = i
        }
        for ((i, d) in listCategory.withIndex()) {
            if (d.id == categoryId) positionCategory = i
        }


        val adapterLevel = ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listLevel)
        val adapterStatus = ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listStatus)
        val adapterCategory =
            ArrayAdapter(requireContext(), R.layout.dropdown_bencana, listCategoryName)

        binding.apply {
            autoCompleteLevel.setText(adapterLevel.getItem(positionLevel))
            autoCompleteStatus.setText(adapterStatus.getItem(positionStatus))
            autoCompleteBencana.setText(adapterCategory.getItem(positionCategory))

            autoCompleteLevel.setAdapter(adapterLevel)
            autoCompleteStatus.setAdapter(adapterStatus)
            autoCompleteBencana.setAdapter(adapterCategory)
        }
    }

    private fun initView() {
        binding.apply {
            val url = Constants.BASE_URL + imageUrl
            Glide.with(requireView()).load(url).into(ivPost)
            etDescription.setText(description)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_update_post -> {
                binding.apply {
                    val description = etDescription.text.toString()
                    var category = 0
                    for (d in listCategories) {
                        if (d.category == autoCompleteBencana.text.toString()) {
                            category = d.id
                        }
                    }
                    val level = autoCompleteLevel.text.toString()
                    val txtStatus = autoCompleteStatus.text.toString()
                    val status = if(txtStatus == "Aktif"){
                        1
                    }else{
                        0
                    }

                    if (description.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Deskripsi tidak boleh kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        updatePost(description, category, level, status)
                    }
                }

            }
        }
    }

    private fun updatePost(description: String, category: Int, level: String, status: Int) {

        val updatePostBody = UpdatePostBody(idPost, description, category, level, status)
        viewModel.updatePost(token, updatePostBody).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    dialog.startDialog()
                    Log.d("UpdatePostFragment", "Loading Update Post")
                }
                is Resource.Success -> {
                    dialog.dismissDialog()
                    Toast.makeText(requireContext(), result.data!!.message, Toast.LENGTH_SHORT)
                        .show()
                    val mBundle = bundleOf(
                        Constants.EXTRA_ID to idPost
                    )
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_updatePostFragment_to_detailPostFragment, mBundle)
                }
            }
        }
    }

}