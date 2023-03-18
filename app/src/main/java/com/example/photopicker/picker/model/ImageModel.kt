package com.example.photopicker.picker.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData


class ImageModel {

    private var initialImage: Bitmap? = null
    private var lastImage: Bitmap? = null
    var listSize: String = "50"
    val imageList: MutableLiveData<ArrayList<PicDataModel>> = MutableLiveData()

    fun setImageUri(images: ArrayList<Bitmap?>) {
        initialImage = images[0]
        lastImage = images[1]
        generateTriangularImageList()
    }



     fun generateTriangularImageList() {
        val list: ArrayList<PicDataModel> = ArrayList()
        for (index in 1..listSize.toInt()) {
            if (isGivenNumberTriangularNumber(index.toDouble())) {
                list.add(PicDataModel(index, initialImage))
            } else {
                list.add(PicDataModel(index, lastImage))
            }
        }
        imageList.value = list
    }


    private fun isGivenNumberTriangularNumber(number: Double): Boolean {
        val result = (kotlin.math.sqrt(8 * number + 1) - 1) / 2
        return result % 1 == 0.0
    }

    fun isSizeValid(): Boolean {
        return (!listSize.isNullOrEmpty() && listSize.toInt() > 1)
    }

}