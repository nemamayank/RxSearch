package com.rxsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rxsearch.R
import com.rxsearch.data.models.Images
import com.rxsearch.databinding.ImageListItemBinding

class ImageAdapter(private val onImageClickListener: OnImageClickListener) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var imageList = ArrayList<Images>()

    fun setImageList(imageList: List<Images>) {
        this.imageList = imageList as ArrayList<Images>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: com.rxsearch.databinding.ImageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImageListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image: Images = imageList[position]
        Glide.with(holder.itemView.context)
            .load(image.link)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .apply(RequestOptions().override(80, 80))
            .into(holder.binding.searchedImage)

        holder.binding.imageName.text = image.description ?: "N/A"
        holder.itemView.setOnClickListener {
            onImageClickListener.onImageClick(image)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun clear() {
        val size: Int = imageList.size
        imageList.clear()
        notifyItemRangeRemoved(0, size)
    }

    interface OnImageClickListener {
        fun onImageClick(images: Images)
    }
}