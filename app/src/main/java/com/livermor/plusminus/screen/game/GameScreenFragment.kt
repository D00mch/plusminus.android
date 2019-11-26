package com.livermor.plusminus.screen.game

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.livermor.plusminus.view.attachBoard
import com.livermor.plusminus.view.bgColor
import trikita.anvil.RenderableView
import com.livermor.plusminus.R
import com.livermor.plusminus.db.AppDb.gameViewState
import com.livermor.plusminus.screen.game.single.SinglePlayerDispatcher

import trikita.anvil.DSL.*

class GameScreenFragment : Fragment() {

    private val dispatcher by lazy { SinglePlayerDispatcher }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Log.i(TAG(), "onCreateView: ")
        return object : RenderableView(container?.context) {
            override fun view() {
                dispatcher.dispatch(GameMsg.Update)
                linearLayout {
                    bgColor(R.color.colorPrimaryDark)
                    size(MATCH, MATCH)
                    orientation(LinearLayout.VERTICAL)
                    gravity(Gravity.CENTER)

                    frameLayout {
                        textView {
                            size(WRAP, WRAP)
                            backgroundColor(R.color.grey)
                            layoutGravity(Gravity.END)
                            text("he: ${gameViewState.state.vrtPoints}")
                        }
                        textView {
                            size(WRAP, WRAP)
                            backgroundColor(R.color.grey)
                            layoutGravity(Gravity.START)
                            text("me: ${gameViewState.state.hrzPoints}")
                        }
                    }

                    attachBoard(gameViewState.state, onClick = { turn, state, x, y ->
                        dispatcher.dispatch(GameMsg.Move(x, y, turn))
                    })

                    button {
                        text("new game")
                        onClick { dispatcher.dispatch(GameMsg.NewGame) }
                    }
                }
            }
        }
    }
}