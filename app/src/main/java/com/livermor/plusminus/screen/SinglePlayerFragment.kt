package com.livermor.plusminus.screen

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.livermor.plusminus.AppDb
import com.livermor.plusminus.R
import com.livermor.plusminus.TAG
import com.livermor.plusminus.model.generateState
import com.livermor.plusminus.model.isValidMove
import com.livermor.plusminus.model.move
import com.livermor.plusminus.rules.onMove
import com.livermor.plusminus.toast
import com.livermor.plusminus.view.attachBoard
import com.livermor.plusminus.view.bgColor
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

class SinglePlayerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG(), "onViewCreated: ")
        Anvil.mount(view, {
            onMove(AppDb.offlineState)
            linearLayout {
                layoutGravity(Gravity.CENTER)
                size(MATCH, WRAP)
                orientation(LinearLayout.VERTICAL)

                attachBoard(AppDb.offlineState, onClick = { turn, state, x, y ->
                    if (turn) {
                        if (AppDb.offlineState.isValidMove(x, y)) {
                            AppDb.offlineState = AppDb.offlineState.move(x, y)
                        }
                    } else {
                        toast("mind your manners!")
                    }
                })

                button {
                    text("reset")
                    onClick { AppDb.offlineState = generateState(6) }
                }
            }
        })
    }
}
