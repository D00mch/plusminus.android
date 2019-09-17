package com.livermor.plusminus.rules

import android.content.Context
import com.livermor.plusminus.model.State
import com.livermor.plusminus.model.anyMoves

fun Context.onMove(state: State) {
    val (board, _, _, _, isHrzTurn, moves) = state
    if (state.anyMoves()) {
        
    }
}