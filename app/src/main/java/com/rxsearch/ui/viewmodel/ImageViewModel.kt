package com.rxsearch.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rxsearch.data.models.Data
import com.rxsearch.data.models.Images

class ImageViewModel : ViewModel() {
    private var imageLiveData = MutableLiveData<List<Images>>()

    fun getImages(dataList: List<Data>) {
        val imageData = ArrayList<Images>()
        try{
            if(dataList.isEmpty()) {
                setImageData(imageData)
                return
            }
            for (item in dataList){
                if(item.images.isEmpty()) {
                    setImageData(imageData)
                    break
                }
                for (imageUrl in item.images){
                    imageData.add(imageUrl)
                }
            }
            setImageData(imageData)
        }catch (e:Exception){
            Log.e("<<Exception>>", e.toString())
            setImageData(imageData)
        }
    }

    private fun setImageData(imageData: ArrayList<Images>) {
        imageLiveData.value = imageData
    }

    fun observeUpdatedData(): LiveData<List<Images>> {
        return imageLiveData
    }
}