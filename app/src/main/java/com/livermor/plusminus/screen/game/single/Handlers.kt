package com.livermor.plusminus.screen.game.single

import com.livermor.plusminus.*
import com.livermor.plusminus.model.*
import com.livermor.plusminus.rules.*
import com.livermor.plusminus.screen.IMsg
import com.livermor.plusminus.screen.EffectHandler
import com.livermor.plusminus.screen.EventHandler
import com.livermor.plusminus.screen.game.GameMsg
import com.livermor.plusminus.screen.game.GameViewState

object SingleMove : EventHandler<GameMsg.Move, GameViewState> {

    override fun invoke(msg: GameMsg.Move, model: GameViewState): GameViewState =
        if (model.state.isHrzTurn) {
            val (x, y, _) = msg
            if (model.state.isValidMove(x, y)) {
                model.copy(state = model.state.move(x, y))
            } else {
                toast("invalid move")
                model
            }
        } else {
            toast("mind your manners!")
            model
        }
}

object SingleNewGame : EventHandler<GameMsg.NewGame, GameViewState> {
    override fun invoke(msg: GameMsg.NewGame, model: GameViewState): GameViewState {
        return GameViewState(state = generateState(6))
    }
}

object SingleOnMoveEffect : EffectHandler<GameMsg.Update, GameViewState, SinglePlayerDispatcher> {

    private var moveRunnable: Runnable? = null

    override fun invoke(msg: IMsg, model: GameViewState, dispatcher: SinglePlayerDispatcher) {
        val state = model.state
        if (state.anyMoves()) {
            if (state.isHrzTurn.not()) {
                moveRunnable?.taskCancel()
                moveRunnable = {
                    val newState = model.copy(state = model.state.moveBot())
                    dispatcher.dispatch(GameMsg.StateChange(newState))
                }.taskAfter(1000L + rand.nextInt(750) + if (state.moves.isEmpty()) 1000L else 0L)
            }
        } else {
            { showResult(state) }.task()
        }
    }

    private fun showResult(state: State) = when (state.onGameEnd(true)) {
        Result.LOSE -> toast("You lose")
        Result.WIN -> toast("You win")
        Result.DRAW -> toast("Draw")
    }
}
