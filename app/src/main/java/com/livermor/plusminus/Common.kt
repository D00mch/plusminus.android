package com.livermor.plusminus

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import java.util.*


@Suppress("FunctionName")
fun Any.TAG(): String = this::class.java.simpleName

val uiHandler = Handler()

val rand = Random()

fun toast(@StringRes res: Int) {
    Toast.makeText(App.context, res, Toast.LENGTH_SHORT).show()
}

fun toast(str: String) {
    Toast.makeText(App.context, str, Toast.LENGTH_SHORT).show()
}

fun Runnable.task(): Runnable = this.apply { uiHandler.post(this) }
fun (() -> Unit).task(): Runnable = Runnable { this() }.task()

fun Runnable.taskAfter(millis: Long): Runnable = apply { uiHandler.postDelayed(this, millis) }

fun (() -> Unit).taskAfter(millis: Long): Runnable = Runnable { this() }.taskAfter(millis)

fun Runnable.taskCancel(): Unit = uiHandler.removeCallbacks(this)

