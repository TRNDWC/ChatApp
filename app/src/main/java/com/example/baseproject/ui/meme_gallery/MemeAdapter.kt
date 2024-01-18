package com.example.baseproject.ui.meme_gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.LayoutImageGalleryItemBinding
import com.example.baseproject.model.Meme

interface OnMemeClickListener {
    fun onMemeClick(binding: LayoutImageGalleryItemBinding, meme: Meme)
}

class MemeAdapter(
    private val mMeme: List<Meme>,
    private val onMemeClickListener: OnMemeClickListener
) : RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {

    inner class MemeViewHolder(
        mMessageItem: LayoutImageGalleryItemBinding,
        onMemeClickListener: OnMemeClickListener
    ) : RecyclerView.ViewHolder(mMessageItem.root) {
        val imgContent = mMessageItem.imageView
        val view = mMessageItem.view
        init {
            mMessageItem.apply {
                imgContent.setOnClickListener {
                    onMemeClickListener.onMemeClick(mMessageItem, mMeme[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val mMessageItem: LayoutImageGalleryItemBinding = LayoutImageGalleryItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MemeViewHolder(mMessageItem, onMemeClickListener)
    }

    override fun getItemCount(): Int {
        return mMeme.size
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val message = mMeme[position]
        Glide.with(holder.itemView.context).load(message.url).placeholder(android.R.color.darker_gray).into(holder.imgContent)
        holder.view.visibility = View.GONE
    }
}