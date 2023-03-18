package com.example.photopicker.picker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photopicker.picker.model.PicDataModel
import com.example.photopicker.databinding.PicItemBinding


class ImageListingRecyclerViewAdapter :
    RecyclerView.Adapter<ImageListingRecyclerViewAdapter.PicItemViewHolder>() {

    private var itemList: List<PicDataModel> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicItemViewHolder {
        return PicItemViewHolder(
            PicItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PicItemViewHolder, position: Int) {
        holder.bind(itemList[position])

    }

    override fun getItemCount() = itemList.size

    fun submitData(list: List<PicDataModel>) {
        itemList = list
        notifyDataSetChanged()
    }


    inner class PicItemViewHolder(private val binding: PicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PicDataModel) {
            binding.imagePosition.text = item.picPosition.toString()
            binding.image.setImageBitmap(item.pic)


        }
    }


}