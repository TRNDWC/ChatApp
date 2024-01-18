package com.example.baseproject.extension

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.validate(
    validator: (String) -> Unit,
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validator(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}


@SuppressLint("ClickableViewAccessibility")
fun EditText.setOnRightDrawableClickListener(onClick: () -> Unit) {
    this.setOnTouchListener { _, event ->
        var hasConsumed = false
        if (this.compoundDrawables[2] != null) {
            val rightDrawable: android.graphics.drawable.Drawable = this.compoundDrawables[2]
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= this.right - rightDrawable.bounds.width()) {
                    onClick()
                    hasConsumed = true
                }
            }
        }
        hasConsumed
    }
}