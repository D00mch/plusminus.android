package com.livermor.plusminus.screen.game.single

import com.livermor.plusminus.db.AppDb.offlineState
import com.livermor.plusminus.db.AppDb.gameViewState
import com.livermor.plusminus.screen.IDispatcher
import com.livermor.plusminus.screen.game.GameMsg


object SinglePlayerDispatcher : IDispatcher<GameMsg> {

    override fun dispatch(msg: GameMsg) = when (msg) {
        is GameMsg.Move -> gameViewState = SingleMove(msg, gameViewState)
        is GameMsg.NewGame -> gameViewState = SingleNewGame(msg, gameViewState)
        is GameMsg.Update -> SingleOnMoveEffect(msg, gameViewState, this)
        is GameMsg.StateChange -> gameViewState = msg.state
    }.also {
        if (offlineState != gameViewState.state) offlineState = gameViewState.state
    }
}
