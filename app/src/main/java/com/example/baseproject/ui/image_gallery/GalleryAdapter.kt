package com.example.baseproject.ui.image_gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.LayoutImageGalleryItemBinding
import com.example.baseproject.model.Image

interface OnImageClickListener {
    fun onImageClick(binding: LayoutImageGalleryItemBinding, image: Image)
}

class GalleryAdapter(
    private val mMessage: MutableList<Image>,
    private val onImageClickListener: OnImageClickListener):
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

        inner class GalleryViewHolder(
            mMessageItem: LayoutImageGalleryItemBinding,
            onImageClickListener: OnImageClickListener
        ) : RecyclerView.ViewHolder(mMessageItem.root) {
            val imgContent = mMessageItem.imageView
            val view = mMessageItem.view
            init {
                mMessageItem.apply {
                    imgContent.setOnClickListener {
                        onImageClickListener.onImageClick(mMessageItem, mMessage[adapterPosition])
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val mMessageItem: LayoutImageGalleryItemBinding = LayoutImageGalleryItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GalleryViewHolder(mMessageItem, onImageClickListener)
    }

    override fun getItemCount(): Int {
        return mMessage.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val message = mMessage[position]
        holder.view.visibility = if (message.isSelected) {
            ViewGroup.VISIBLE
        } else {
            ViewGroup.GONE
        }
        Glide.with(holder.itemView.context).load(message.url).into(holder.imgContent)
    }

    fun getSelectedImage(): MutableList<Image> {
        val list = mutableListOf<Image>()
        for (image in mMessage) {
            if (image.isSelected) {
                list.add(image)
            }
        }
        return list
    }
}