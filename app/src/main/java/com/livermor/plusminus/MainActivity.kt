package com.livermor.plusminus

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.bulk
import com.livermor.plusminus.model.RequestRegister
import com.livermor.plusminus.network.PublicApi
import com.livermor.plusminus.network.on
import com.livermor.plusminus.screen.Screen
import com.livermor.plusminus.screen.attachSinglePlayer
import com.livermor.plusminus.view.RegisterView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import trikita.anvil.Anvil
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Anvil.mount(findViewById<View>(android.R.id.content)) {
            when (AppDb.screen) {
                Screen.SINGLE -> attachSinglePlayer()
                else -> throw IllegalStateException("unknown screen")
            }
        }
    }

    // TODO: clear below

    val retrofit = Retrofit.Builder()
        .baseUrl("https://plusminus.me/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(PublicApi::class.java)

    fun register() {
        setContentView(
            RegisterView(this) { login, password, reg: RegisterView ->
                service.register(RequestRegister(login, password, password))
                    .on(
                        { _, t ->
                            Log.e(TAG(), "on register", t)
                            reg.onRegister(false)
                        },
                        { _, r ->
                            reg.onRegister(true)
                            Log.i(TAG(), "registered" + r.body())
                            Log.i(TAG(), "ring-session ${r.headers().get("Set-Cookie")}")
                            r.headers().get("Set-Cookie")?.let {
                                AppDb.bulk {
                                    session = it
                                    identity = login
                                }
                            }
                        })
            }
        )
    }

    fun loadBoard() {
        service.gameState("arturdumchev").on(
            failure = { _, t -> Log.w("bob", "error $t") },
            response = { _, response -> Log.w("bob", response.body().toString()) }
        )
    }
}
