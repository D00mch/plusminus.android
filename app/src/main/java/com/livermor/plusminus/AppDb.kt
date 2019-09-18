package com.livermor.plusminus

import android.util.Log
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref
import com.chibatching.kotpref.gsonpref.GsonPref
import com.google.gson.reflect.TypeToken
import com.livermor.plusminus.model.State
import com.livermor.plusminus.model.generateState
import com.livermor.plusminus.screen.Screen
import trikita.anvil.Anvil
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object AppDb : KotprefModel() {

    var identity: String by AnvilUpdateDelegate(stringPref())
    var session: String by AnvilUpdateDelegate(stringPref())

    var screen: Screen by AnvilUpdateDelegate(enumOrdinalPref(Screen.SINGLE, "screen"))

    var offlineState: State by AnvilUpdateDelegate(
        GsonPref(
            object : TypeToken<State>() {}.type,
            generateState(5), "state", false
        )
    )

    var id: Int by AnvilUpdateDelegate(intPref(default = 100, key = "layer_iq"))

    var appRated: Boolean by AnvilUpdateDelegate(
        booleanPref(default = false, key = "did user rate the app"))

    fun persist() = pendingUpdates.values.forEach { it() }
}

private val pendingUpdates: MutableMap<String, () -> Unit> = HashMap()

private class AnvilUpdateDelegate<T : Any?>(val prefDelegate: ReadWriteProperty<KotprefModel, T>) :
    ReadWriteProperty<KotprefModel, T> {

    private var cache: T? = null

    override fun getValue(thisRef: KotprefModel, property: KProperty<*>): T {
        return cache ?: prefDelegate.getValue(thisRef, property)
    }

    override fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
        pendingUpdates[property.name] = { prefDelegate.setValue(thisRef, property, value) }
        cache = value
        task { Anvil.render() }
    }
}
