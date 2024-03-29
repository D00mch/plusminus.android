package com.livermor.plusminus

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        Kotpref.init(this)
        Kotpref.gson = Gson()
    }

    companion object {
        lateinit var context: App
    }
}