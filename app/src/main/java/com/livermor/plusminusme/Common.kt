package com.livermor.plusminusme

import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


fun Any.TAG(): String = this::class.java.simpleName

val rand = Random()

// view

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

// network

fun <T> Call<T>.on(
    failure: (call: Call<T>, t: Throwable) -> Unit,
    response: (call: Call<T>, response: Response<T>) -> Unit
) {
    enqueue(
        object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                failure(call, t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                response(call, response)
            }
        })
}
