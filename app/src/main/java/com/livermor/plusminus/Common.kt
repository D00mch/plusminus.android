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

fun task(f: () -> Unit): Runnable = Runnable { f() }.apply { uiHandler.post(this) }

fun taskAfter(millis: Long, f: () -> Unit): Runnable
        = Runnable { f() }.also { task -> uiHandler.postDelayed(task, millis) }

fun taskCancel(task: Runnable): Unit = uiHandler.removeCallbacks(task)

