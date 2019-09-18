package com.livermor.plusminus

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref
import com.chibatching.kotpref.gsonpref.GsonPref
import com.google.gson.reflect.TypeToken
import com.livermor.plusminus.model.State
import com.livermor.plusminus.model.generateState
import com.livermor.plusminus.screen.Screen

object AppDb : KotprefModel() {

    var identity: String by stringPref()
    var session: String by stringPref()

    var screen: Screen by enumOrdinalPref(Screen.SINGLE, "screen")
    
    var offlineState: State by GsonPref(
        object : TypeToken<State>(){}.type,
        generateState(5), "state", false
    )

    var id: Int by intPref(default = 100, key = "player_iq")

    var appRated: Boolean by booleanPref(default = false, key = "did user rate the app")
}