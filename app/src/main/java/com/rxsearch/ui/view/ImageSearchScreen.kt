package com.rxsearch.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rxsearch.R
import com.rxsearch.data.api.RetrofitInstance.api
import com.rxsearch.data.models.Data
import com.rxsearch.data.models.Images
import com.rxsearch.databinding.ImageListBinding
import com.rxsearch.di.repositories.ViewModelFactory
import com.rxsearch.ui.adapter.ImageAdapter
import com.rxsearch.ui.viewmodel.ImageViewModel
import com.rxsearch.utility.Common.showProgressBar
import com.rxsearch.utility.Constants.DEFAULT_IMAGE_SEARCH
import com.rxsearch.utility.Constants.IMAGE_DETAIL_URL
import com.rxsearch.utility.Constants.WINDOW_VIRAL
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ImageSearchScreen : AppCompatActivity(), ImageAdapter.OnImageClickListener {
    private lateinit var binding: ImageListBinding
    private lateinit var viewModel: ImageViewModel
    private lateinit var imageAdapter: ImageAdapter
    private var compositeDisposable = CompositeDisposable()

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
                fetchImages(query!!, "", WINDOW_VIRAL)
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
            ViewModelFactory()
        )[ImageViewModel::class.java]
        fetchImages(DEFAULT_IMAGE_SEARCH, "", WINDOW_VIRAL)
    }

    private fun fetchImages(searchText: String, sort: String, window: String){
        Log.d("<fetchImages>", "API Fetch")
        showProgressBar(binding.progressBar, true)
        binding.rvImages.visibility = View.GONE

        compositeDisposable.add(api.getImagesRx(searchText, sort, window)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { result -> result.data }
            .subscribe(this::handleResults, this::handleError))
    }

    private fun handleResults(searchImage: List<Data>) {
        Log.d("<handleResults>", "API Success")
        binding.rvImages.visibility = View.VISIBLE
        showProgressBar(binding.progressBar, false)
        if (searchImage.isNotEmpty()) {
            viewModel.getImages(searchImage)
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.d("<handleError>", throwable.message.toString())
        binding.rvImages.visibility = View.VISIBLE
        showProgressBar(binding.progressBar, false)
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}