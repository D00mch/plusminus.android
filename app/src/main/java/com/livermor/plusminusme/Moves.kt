package com.livermor.plusminusme

import com.livermor.plusminusme.model.State
import com.livermor.plusminusme.model.anyMoves
import com.livermor.plusminusme.model.move
import com.livermor.plusminusme.model.moveVal

private fun moveMax(state: State): State {
    val maxMove: Int? = state.validMoves.maxBy { mv -> state.moveVal(mv) }
    return maxMove?.let { state.move(maxMove) } ?: state
}

private fun pointsDiff(hrzTurn: Boolean, state: State): Int =
    state.run { hrzPoints - vrtPoints }.let { if (hrzTurn) it else -it }

private fun predict(state: State, turnsAhead: Int): State = when {
    state.anyMoves().not() -> state.copy(isHrzTurn = state.isHrzTurn.not())
    turnsAhead <= 0 -> moveMax(state)
    else -> state.validMoves
        .map(state::move)
        .map { st -> predict(st, turnsAhead.dec()) }
        .maxBy { st -> pointsDiff(state.isHrzTurn, st) } ?: state
}

private fun scenarios(state: State, turnsAhead: Int): List<State> =
    state.validMoves
        .map(state::move)
        .map { st -> predict(st, turnsAhead.dec()) }

fun State.moveBot(prediction: Int = 3): State {
    val pmvs = predict(this, prediction).moves
    val mv = pmvs[this.moves.count()]
    return this.move(mv)
}

/**
 * Prefers early win; best state at first
 */
private fun statesComparator(hrz: Boolean): Comparator<State> =
    Comparator { s1, s2 ->
        val m1 = s1.moves.count()
        val m2 = s2.moves.count()
        val d1 = pointsDiff(hrz, s1)
        val d2 = pointsDiff(hrz, s2)
        val n = m1 - m2
        if (0 < d1 && 0 < d2) {
            if (n == 0) d2 - d1 else n
        } else {
            d2 - d1
        }
    }

/**
 * Like [moveBot], but prevents suicide moves and prefers quick wins
 */
fun moveBotSafe(state: State, prediction: Int = 3) : State {
    val states = scenarios(state, prediction)
        .sortedWith(statesComparator(state.isHrzTurn))
    val pmvs: List<Int> = states.first().moves
    val mv: Int = state.moves.count().let { pmvs[it] }
    return state.move(mv)
}
