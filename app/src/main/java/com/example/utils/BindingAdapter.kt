package com.example.utils

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter
import com.example.photopicker.R
import com.google.android.material.textview.MaterialTextView


object LastClickTimeSingleton {
    var lastClickTime: Long = 0
}


fun View.setClickWithDebounce(action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {

        override fun onClick(v: View) {
            v.context.hideKeyboardManager(v)
            if (SystemClock.elapsedRealtime() - LastClickTimeSingleton.lastClickTime < 1000L) return
            else action()
            LastClickTimeSingleton.lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

@BindingAdapter("listCount")
fun setImageCount(
    view: MaterialTextView,
    count: String?
) {
    view.text = view.rootView.context.resources.getString(R.string.image_count,count)
}

@BindingAdapter("onClickWithDebounce")
fun onClickWithDebounce(view: View, listener: View.OnClickListener) {
    view.setClickWithDebounce {
        view.context.hideKeyboardManager(view)
        listener.onClick(view)
    }
}
