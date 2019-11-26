package com.livermor.plusminus.db

import com.chibatching.kotpref.KotprefModel
import com.livermor.plusminus.task
import trikita.anvil.Anvil
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val pendingUpdates: MutableMap<String, () -> Unit> = HashMap()

class AnvilUpdateDelegate<T : Any?>(private val prefDelegate: ReadWriteProperty<KotprefModel, T>) :
    ReadWriteProperty<KotprefModel, T> {

    private var cache: T? = null

    override fun getValue(thisRef: KotprefModel, property: KProperty<*>): T {
        return cache ?: prefDelegate.getValue(thisRef, property)
    }

    override fun setValue(thisRef: KotprefModel, property: KProperty<*>, value: T) {
        pendingUpdates[property.name] = { prefDelegate.setValue(thisRef, property, value) }
        cache = value
        { Anvil.render() }.task()
    }
}