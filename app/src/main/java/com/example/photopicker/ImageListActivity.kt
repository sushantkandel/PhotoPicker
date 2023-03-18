package com.example.photopicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photopicker.databinding.PhotoListActivityMainBinding

class ImageListActivity : AppCompatActivity() {

    lateinit var photoListBinding: PhotoListActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoListBinding = PhotoListActivityMainBinding.inflate(layoutInflater)
        setContentView(photoListBinding.root)

    }
}