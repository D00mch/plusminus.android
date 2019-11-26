package com.livermor.plusminus.screen.game

import com.livermor.plusminus.model.State
import com.livermor.plusminus.screen.IMsg
import com.livermor.plusminus.screen.IModel

sealed class GameMsg : IMsg {
    data class Move(val x: Int, val y: Int, val turn: Boolean) : GameMsg()
    object NewGame : GameMsg()
    object Update : GameMsg()
    data class StateChange(val state: GameViewState) : GameMsg()
}

data class GameViewState(
    val state: State
) : IModel

