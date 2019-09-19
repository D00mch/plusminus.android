package com.livermor.plusminus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.bulk
import com.livermor.plusminus.model.RequestRegister
import com.livermor.plusminus.network.PublicApi
import com.livermor.plusminus.network.on
import com.livermor.plusminus.screen.Screen
import com.livermor.plusminus.screen.attachSinglePlayer
import com.livermor.plusminus.view.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL
import java.lang.IllegalStateException
import trikita.anvil.DSL.*

class MainActivity : AppCompatActivity() {

    private val register: FrameLayout by lazy {
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Anvil.mount(findViewById<View>(android.R.id.content)) {
            frameLayout {
                frameLayout {
                    visibility(Screen.SINGLE == AppDb.screen)
                    attachSinglePlayer()
                }

//                when (AppDb.screen) {
//                    Screen.SINGLE -> attachSinglePlayer()
//                    Screen.AUTH -> {
//                        Anvil.currentView<FrameLayout>().addView(register)
//                    }
//                    else -> throw IllegalStateException("unknown screen")
//                }

                val bottomHeight = dip(66)
                linearLayout {
                    size(MATCH, bottomHeight)
                    layoutGravity(Gravity.BOTTOM)
                    orientation(LinearLayout.HORIZONTAL)
                    z(30f)
                    padding(dip(8))

                    bottomIcon(Screen.SINGLE, R.drawable.ic_home) { AppDb.screen = it }
                    bottomIcon(Screen.SETTINGS, R.drawable.ic_settings) { AppDb.screen = it }
                    bottomIcon(Screen.MULTIPLAYER, R.drawable.ic_people) { AppDb.screen = it }
                    bottomIcon(Screen.ABOUT, R.drawable.ic_info) { AppDb.screen = it }
                    bottomIcon(Screen.AUTH, R.drawable.ic_person) { AppDb.screen = it }
                }
                view {
                    size(MATCH, 1)
                    layoutGravity(Gravity.BOTTOM)
                    Anvil.currentView<View>().bgColor(android.R.color.white)
                    margin(0, bottomHeight)
                }
            }
        }
        startActivity(Intent(this, BottomNavigationActivity::class.java))
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
    }

    fun loadBoard() {
        service.gameState("arturdumchev").on(
            failure = { _, t -> Log.w("bob", "error $t") },
            response = { _, response -> Log.w("bob", response.body().toString()) }
        )
    }
}
