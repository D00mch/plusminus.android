package com.livermor.plusminus

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chibatching.kotpref.bulk
import com.livermor.plusminus.db.AppDb
import com.livermor.plusminus.model.RequestRegister
import com.livermor.plusminus.network.PublicApi
import com.livermor.plusminus.network.on
import com.livermor.plusminus.screen.data.DataScreenFragment
import com.livermor.plusminus.screen.game.GameScreenFragment
import com.livermor.plusminus.view.RegisterView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val gameScreen by lazy { GameScreenFragment() }
    private val dataScreen by lazy { DataScreenFragment() }
    private var prevFrag: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.flRootContent, dataScreen, dataScreen.javaClass.name)
            .add(R.id.flRootContent, gameScreen, gameScreen.javaClass.name)
            .commit()
        prevFrag = gameScreen

        tvDataScreen.setUpScreenClick(dataScreen)
        tvGameScreen.setUpScreenClick(gameScreen)
    }

    private fun View.setUpScreenClick(screen: Fragment) = setOnClickListener {
        val transaction = supportFragmentManager.beginTransaction()
        prevFrag?.let { transaction.hide(it) }
        transaction.show(screen).commit()
        prevFrag = screen
    }

    override fun onStop() {
        super.onStop()
        AppDb.persist()
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
