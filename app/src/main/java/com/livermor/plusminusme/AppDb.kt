package com.livermor.plusminusme

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.GsonPref
import com.google.gson.reflect.TypeToken
import com.livermor.plusminusme.model.State
import com.livermor.plusminusme.model.generateState

object AppDb : KotprefModel() {

    var identity: String by stringPref()
    var session: String by stringPref()

    var offlineState: State by GsonPref(
        object : TypeToken<State>(){}.type,
        generateState(5), "state", false
    )
}