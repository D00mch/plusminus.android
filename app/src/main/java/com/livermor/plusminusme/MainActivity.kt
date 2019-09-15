package com.livermor.plusminusme

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.bulk
import com.livermor.plusminusme.model.RequestRegister
import com.livermor.plusminusme.model.generateState
import com.livermor.plusminusme.network.PublicApi
import com.livermor.plusminusme.view.BoardView
import com.livermor.plusminusme.view.RegisterView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://plusminus.me/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(PublicApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       //  AppDb.offlineState = generateState(6)

        setContentView(
            BoardView(
                this, AppDb.offlineState,
                onClick = { turn, state, x, y ->
                    Log.i(TAG(), "board clicked with x $x, y $y")
                })
        )

        println("session ${AppDb.session}")
        println("session ${AppDb.session}")
    }

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
