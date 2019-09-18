package com.livermor.plusminus.rules

import com.livermor.plusminus.*
import com.livermor.plusminus.model.State
import com.livermor.plusminus.model.anyMoves
import trikita.anvil.Anvil

fun onMove(state: State) {
    val (board, _, _, _, isHrzTurn, moves) = state
    if (state.anyMoves()) {
        if (isHrzTurn.not()) {
            taskAfter(500L + rand.nextInt(750)) {
                AppDb.offlineState = AppDb.offlineState.moveBot()
                Anvil.render()
            }
        }
    } else {
        task { showResult(state) }
    }
}

private fun showResult(state: State) = when (state.onGameEnd(true)) {
    Result.LOSE -> toast("You lose")
    Result.WIN -> toast("You win")
    Result.DRAW -> toast("Draw")
}
