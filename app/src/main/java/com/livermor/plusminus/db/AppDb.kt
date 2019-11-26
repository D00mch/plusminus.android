package com.livermor.plusminus.db

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.GsonPref
import com.google.gson.reflect.TypeToken
import com.livermor.plusminus.model.State
import com.livermor.plusminus.model.generateState
import com.livermor.plusminus.screen.game.GameViewState

object AppDb : KotprefModel() {

    var identity: String by AnvilUpdateDelegate(stringPref())
    var session: String by AnvilUpdateDelegate(stringPref())

    var offlineState: State by AnvilUpdateDelegate(
        GsonPref(
            object : TypeToken<State>() {}.type,
            generateState(5), "offline state", false
        )
    )

    var gameViewState: GameViewState by AnvilUpdateDelegate(
        GsonPref(
            object : TypeToken<GameViewState>(){}.type,
            GameViewState(offlineState), "game view state", false
        )
    )

    var id: Int by AnvilUpdateDelegate(intPref(default = 100, key = "layer_iq"))

    var appRated: Boolean by AnvilUpdateDelegate(
        booleanPref(default = false, key = "did user rate the app")
    )


    fun persist() = pendingUpdates.values.forEach { it() }
}

