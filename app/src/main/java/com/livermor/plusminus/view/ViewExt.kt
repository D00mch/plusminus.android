package com.livermor.plusminus.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
}

fun View.bgColor(@ColorRes r: Int) {
    setBackgroundColor(ContextCompat.getColor(context, r))
}

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

inline fun <T> ViewGroup.map(action: (idx: Int, view: View) -> T): List<T> =
    (0 until childCount)
        .map { getChildAt(it) }
        .mapIndexed { i, view -> action(i, view) }
