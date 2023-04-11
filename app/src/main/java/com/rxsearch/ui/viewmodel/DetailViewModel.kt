package com.rxsearch.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
/**
 * Intentionally kept unused for future ready implementation
 */
class DetailViewModel : ViewModel()  {
    var showProgressBar = MutableLiveData<Boolean>()
    fun showDetailImage(imageURL:String) {
    }
}