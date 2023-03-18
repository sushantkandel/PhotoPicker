package com.example.photopicker.picker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photopicker.picker.model.ImageModel
import io.reactivex.rxjava3.core.Observable


class ImageListingViewModel : ViewModel() {
    val model = ImageModel()

    val listSizeButtonStatus = MutableLiveData<Boolean>()

    fun setPickedImages(images: ArrayList<Bitmap?>) {
        model.setImageUri(images)
    }



    @SuppressLint("CheckResult")
    fun onSizeListFieldReady(
        listSizeViewBinding: Observable<CharSequence>
    ) {
        listSizeViewBinding.subscribe {
            if(!it.isNullOrEmpty()) {
                model.listSize = it.toString()
                listSizeButtonStatus.value = model.isSizeValid()
            }else{
                listSizeButtonStatus.value=false
            }
        }
    }


}