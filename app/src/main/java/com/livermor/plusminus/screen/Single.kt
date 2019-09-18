package com.livermor.plusminus.screen

import android.widget.LinearLayout
import com.livermor.plusminus.AppDb
import com.livermor.plusminus.model.generateState
import com.livermor.plusminus.model.move
import com.livermor.plusminus.rules.onMove
import com.livermor.plusminus.view.attachBoard
import trikita.anvil.BaseDSL.MATCH
import trikita.anvil.DSL.*

fun attachSinglePlayer() {
    onMove(AppDb.offlineState)
    linearLayout {
        size(MATCH, MATCH)
        orientation(LinearLayout.VERTICAL)

        attachBoard(AppDb.offlineState, onClick = { turn, state, x, y ->
            AppDb.offlineState = AppDb.offlineState.move(x, y)
        })

        button {
            text("reset")
            onClick { AppDb.offlineState = generateState(6) }
        }
    }
}
