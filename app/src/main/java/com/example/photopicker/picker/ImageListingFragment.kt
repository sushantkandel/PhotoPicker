package com.example.photopicker.picker

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.permissionHandler.QuickPermissionsOptions
import com.example.permissionHandler.runWithPermissions
import com.example.photopicker.R
import com.example.photopicker.databinding.FragmentPhotoListingBinding
import com.example.photopicker.picker.model.PicDataModel
import com.example.photopicker.picker.adapter.ImageListingRecyclerViewAdapter
import com.example.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable


class ImageListingFragment : Fragment() {

    private lateinit var viewModel: ImageListingViewModel

    private lateinit var binding: FragmentPhotoListingBinding

    private var adapter: ImageListingRecyclerViewAdapter? = null

    private lateinit var dialogBuilder: AlertDialog.Builder

    private var listEditBottomSheet: ConstraintLayout? = null

    private lateinit var listEditBottomSheetBehavior: BottomSheetBehavior<View>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ImageListingViewModel::class.java]

        listEditBottomSheet = binding.clBottomSheet

        listEditBottomSheetBehavior = BottomSheetBehavior.from(listEditBottomSheet!!)

        listEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.onSizeListFieldReady(getMobileNumberViewBinding())

        observeButtonClickEvent()

        observeImageList()
    }

    private fun getMobileNumberViewBinding(): Observable<CharSequence> {
        return binding.etListSize.textChanges().skipInitialValue()
    }

    private fun observeImageList() {
        viewModel.model.imageList.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.clImageCount.visibility = View.VISIBLE
                setImageCount(binding.tvImageCount, it.size.toString())
                initializeRecyclerView(it)
            } else {
                binding.clImageCount.visibility = View.GONE
            }
        }

    }


    private fun observeButtonClickEvent() {
        binding.fabImgBtn.setClickWithDebounce {
            openGallery()
        }

        binding.ivEditIcon.setClickWithDebounce {
            listEditBottomSheet?.visibility=View.VISIBLE
            listEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.cancelImageBottomSheet.setClickWithDebounce {
            hideButtonSheet()
        }

        viewModel.listSizeButtonStatus.observe(viewLifecycleOwner) {
            binding.btnApply.isEnabled = it
        }

        binding.btnApply.setClickWithDebounce {
            viewModel.model.generateTriangularImageList()
            hideButtonSheet()
        }

    }

    private fun hideButtonSheet() {
        listEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.listSize.clearFocus()
        listEditBottomSheet?.visibility=View.INVISIBLE
    }

    private fun openGallery() {
        val permissionOptions = QuickPermissionsOptions()
        reqStoragePermission(permissionOptions)
    }

    private fun reqStoragePermission(quickPermissionsOptions: QuickPermissionsOptions) =
        runWithPermissions(
            Manifest.permission.CAMERA,
            *setRequiredReadStoragePermission(),
            options = quickPermissionsOptions
        ) {

            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            resultLauncher.launch(Intent.createChooser(galleryIntent, "Select images"))

        }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val imageList: ArrayList<Bitmap?> = ArrayList()
                if (data?.clipData != null && data.clipData!!.itemCount == 2) {

                    data.clipData?.getItemAt(0)?.uri?.let {
                        val bitmap =
                            reduceBitmapSize(getBitmapFromUri(it, this.requireContext()), 921600)
                        imageList.add(bitmap)
                    }
                    data.clipData?.getItemAt(1)?.uri?.let {
                        val bitmap =
                            reduceBitmapSize(getBitmapFromUri(it, this.requireContext()), 921600)
                        imageList.add(bitmap)

                    }
                    viewModel.setPickedImages(imageList)
                } else {
                    initiateDialog()
                }
            }
        }


    private fun initializeRecyclerView(list: ArrayList<PicDataModel>) {

        val recyclerView = binding.imageRecyclerView
        adapter = ImageListingRecyclerViewAdapter()
        val viewManager = GridLayoutManager(view?.context, 2)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = adapter
        adapter?.submitData(list)
    }

    private fun initiateDialog() {
        dialogBuilder = AlertDialog.Builder(this.context)
        with(dialogBuilder) {
            this.setTitle(resources.getString(R.string.app_name))
            this.setMessage(resources.getString(R.string.invalid_no_of_images))
            this.setCancelable(true)
            this.setPositiveButton(
                resources.getString(R.string.ok)
            ) { dialog, which -> openGallery() }
            this.show()
        }

    }

}