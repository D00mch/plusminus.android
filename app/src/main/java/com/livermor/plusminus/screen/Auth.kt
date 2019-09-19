package com.livermor.plusminus.screen

import com.livermor.plusminus.AppDb
import trikita.anvil.BaseDSL.MATCH
import trikita.anvil.DSL.*

fun  auth() {
    linearLayout {
        if (AppDb.identity.isNotEmpty()) {

        } else {

        }
    }
}