package com.rxsearch.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rxsearch.data.api.ImageApiHelper
import com.rxsearch.data.api.RetrofitInstance
import com.rxsearch.data.api.Status
import com.rxsearch.data.models.Images
import com.rxsearch.di.repositories.ViewModelFactory
import com.rxsearch.ui.viewmodel.ImageViewModel
import com.rxsearch.utility.Common.showProgressBar
import com.rxsearch.utility.Constants.DEFAULT_IMAGE_SEARCH
import com.rxsearch.utility.Constants.IMAGE_DETAIL_URL
import com.rxsearch.utility.Constants.WINDOW_VIRAL
import com.rxsearch.R
import com.rxsearch.databinding.ImageListBinding
import com.rxsearch.ui.adapter.ImageAdapter

class ImageSearchScreen : AppCompatActivity(), ImageAdapter.OnImageClickListener {
    private lateinit var binding: ImageListBinding
    private lateinit var viewModel: ImageViewModel
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        viewModel.observeUpdatedData().observe(this) { imageList ->
            if (!imageList.isNullOrEmpty()) {
                imageAdapter.setImageList(imageList)
            } else {
                clearImageAdapter()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                imageSearchCall(query!!, WINDOW_VIRAL)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun init() {
        prepareRecyclerView()
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ImageApiHelper(RetrofitInstance.api))
        )[ImageViewModel::class.java]
        imageSearchCall(DEFAULT_IMAGE_SEARCH, WINDOW_VIRAL)
    }

    private fun imageSearchCall(searchText: String, window: String) {
        viewModel.getSearchedImage(searchText, window).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.rvImages.visibility = View.VISIBLE
                        showProgressBar(binding.progressBar, false)
                        resource.data?.let { searchImage ->
                            if (searchImage.data.isNotEmpty()) {
                                viewModel.getImages(searchImage.data)
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.rvImages.visibility = View.VISIBLE
                        showProgressBar(binding.progressBar, false)
                        clearImageAdapter()
                    }
                    Status.LOADING -> {
                        showProgressBar(binding.progressBar, true)
                        binding.rvImages.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun prepareRecyclerView() {
        imageAdapter = ImageAdapter(this)
        binding.rvImages.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = imageAdapter
        }
    }

    override fun onImageClick(images: Images) {
        startActivity(Intent(this, ImageDetailScreen::class.java).apply {
            putExtra(IMAGE_DETAIL_URL, images.link)
        })
    }

    private fun clearImageAdapter(){
        imageAdapter.clear()
        Toast.makeText(
            applicationContext,
            getString(R.string.content_unavailable),
            Toast.LENGTH_SHORT ).show()
    }
}