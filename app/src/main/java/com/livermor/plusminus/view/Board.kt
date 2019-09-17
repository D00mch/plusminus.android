package com.livermor.plusminus.view

import com.livermor.plusminus.model.isValidMove
import com.livermor.plusminus.model.rowsCount
import trikita.anvil.DSL.*
import android.content.res.Resources
import android.graphics.Typeface
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import com.livermor.plusminus.R
import com.livermor.plusminus.model.State
import com.livermor.plusminus.model.xy2index
import trikita.anvil.Anvil
import kotlin.math.min

/**
 * @return [GridLayout] with [State].board cells represented as TextViews
 */
fun board(
    state: State,
    onClick: (turn: Boolean, state: State, x: Int, y: Int) -> Unit
) {
    val (board, _, _, _, isHrzTurn, moves) = state
    val screenMax = Resources.getSystem().displayMetrics.run {
       min(widthPixels.toFloat(), heightPixels * 0.8f)
    }
    val cellDiam = screenMax / board.rowSize
    val margin = dip(3)

    gridLayout {
        clipChildren(true)
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
                    Anvil.currentView<TextView>().run {
                        bgColor(color(valid, turn))
                        layoutParams = (layoutParams as GridLayout.LayoutParams).apply {
                            width = (cellDiam - 2 * margin).toInt()
                            height = (cellDiam - 2 * margin).toInt()
                            setMargins(margin, margin, margin, margin)
                        }
                    }
                    textSize((dip(30 - board.rowSize)).toFloat())
                    val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    typeface(boldTypeface)
                    padding(1)
                    onClick { onClick(turn, state, x, y) }
                    text(v.toString())
                    gravity(Gravity.CENTER)
                    alpha(if (hidden) 0f else 1f)
                }
            }
        }
    }
}

private fun color(valid: Boolean, turn: Boolean): Int = when {
    valid && turn -> R.color.blue
    valid -> R.color.red
    else -> R.color.grey
}