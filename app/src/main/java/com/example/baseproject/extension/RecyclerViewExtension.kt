package com.example.baseproject.extension

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToBottom() {
    this.adapter?.itemCount.takeIf { it != null && it > 0 }?.let {
        this.scrollToPosition(this.adapter?.itemCount ?: 0)
        this.postDelayed({
            this.smoothScrollToPosition(it - 1)
        },100)
    }
}
