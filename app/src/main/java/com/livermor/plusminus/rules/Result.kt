package com.livermor.plusminus.rules

import com.livermor.plusminus.model.State

enum class Result {
    WIN, LOSE, DRAW
}

fun State.onGameEnd(usrHrz: Boolean): Result {
    val (_, _, hrzP, vrtP, _, _) = this
    return if (hrzP == vrtP) {
        Result.DRAW
    } else {
        val hrzWins = hrzP > vrtP
        if (hrzWins == usrHrz) Result.WIN else Result.LOSE
    }
}

