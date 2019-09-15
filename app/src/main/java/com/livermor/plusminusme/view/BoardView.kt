package com.livermor.plusminusme.view

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import com.livermor.plusminusme.R
import com.livermor.plusminusme.TAG
import com.livermor.plusminusme.afterMeasured
import com.livermor.plusminusme.bgColor
import com.livermor.plusminusme.model.State
import com.livermor.plusminusme.model.isValidMove
import com.livermor.plusminusme.model.rowsCount
import com.livermor.plusminusme.model.xy2index
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView


class BoardView(
    context: Context,
    private val state: State,
    private val onClick: (turn: Boolean, state: State, x: Int, y: Int) -> Unit
) : RenderableView(context) {

    override fun view() {
        val (board, start, _, _, isHrzTurn, moves) = state
        Log.i(TAG(), "state is $state")

        gridLayout {
            clipChildren = false
            columnCount(board.rowSize)
            rowCount(board.rowSize)

            for (y in 0 until state.rowsCount()) {
                for (x in 0 until state.rowsCount()) {
                    val i = board.xy2index(x, y)
                    val valid = state.isValidMove(i)
                    val turn = valid && isHrzTurn
                    val hidden = moves.contains(i)
                    val v = board[i]

                    textView {
                        textSize((dip(30 - board.rowSize)).toFloat())
                        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        typeface(boldTypeface)
                        padding(1)
                        onClick { onClick(turn, state, x, y) }
                        text(v.toString())
                        gravity(Gravity.CENTER)
                        alpha = if (hidden) 0f else 1f
                        val colorRes = when {
                            valid && turn -> R.color.blue
                            valid -> R.color.red
                            else -> R.color.grey
                        }
                        Anvil.currentView<TextView>().bgColor(colorRes)
                    }
                }
            }

            Anvil.currentView<GridLayout>().afterMeasured {
                val margin = 6
                val pLength = if (width >= height) height else width
                layoutParams = layoutParams.apply {
                    width = pLength
                    height = pLength
                }
                val w = pLength / board.rowSize
                val h = pLength / board.rowSize
                for (c in 0 until childCount) {
                    val view = getChildAt(c)
                    val lp = (view.layoutParams as GridLayout.LayoutParams).apply {
                        width = w - 2 * margin
                        height = h - 2 * margin
                        setMargins(margin, margin, margin, margin)
                    }
                    view.layoutParams = lp
                }
            }
        }
    }
}