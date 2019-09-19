package com.livermor.plusminus.view

import android.animation.ValueAnimator
import trikita.anvil.DSL.*
import android.content.res.Resources
import android.graphics.Typeface
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import com.livermor.plusminus.R
import com.livermor.plusminus.model.*
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL
import kotlin.math.min

/**
 * @return [GridLayout] with [State].board cells represented as TextViews
 */
inline fun attachBoard(
    state: State,
    crossinline onClick: (turn: Boolean, state: State, x: Int, y: Int) -> Unit
) {
    val (board, _, _, _, isHrzTurn, moves) = state
    val screenMax = Resources.getSystem().displayMetrics.run {
        min(widthPixels.toFloat(), heightPixels * 0.8f)
    } - defaultMar
    val cellDiam = screenMax / board.rowSize
    val margin = dip(3)

    val i2anim = state.animation()

    gridLayout {
        size(MATCH, WRAP)
        padding(defaultMar/2)
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
                val animDependentSetUp: (tv: TextView) -> Unit = { tv ->
                    tv.bgColor(cellColor(valid, turn))
                    tv.setOnClickListener { onClick(turn, state, x, y) }
                }
                textView {
                    val tv = Anvil.currentView<TextView>()
                    tv.run {
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
                    text(v.toString())
                    gravity(Gravity.CENTER)
                    alpha(if (hidden) 0f else 1f)
                    i2anim[i]?.let { animF ->
                        animF(tv) { animDependentSetUp(tv) }
                    } ?: run { animDependentSetUp(tv) }
                }
            }
        }
    }
}

fun cellColor(valid: Boolean, turn: Boolean): Int = when {
    valid && turn -> R.color.blue
    valid -> R.color.red
    else -> R.color.grey
}

typealias Animator = (view: View, callback: () -> Unit) -> Unit

const val ANIM_START_DELAY = 300L
const val SHIFT_DELAY = 60L

fun State.animation(): SparseArray<Animator> {
    val (board, start, _, _, isHrzTurn, moves) = this
    val lastMove: Int = moves.lastOrNull() ?: start
    val (xL, yL) = board.coords(lastMove)
    return animateInARow(xL, yL, rowsCount(), isHrzTurn)
}

private fun animateInARow(x: Int, y: Int, rowSize: Int, hrzTurn: Boolean): SparseArray<Animator> {
    val items = SparseArray<Animator>()

    val expCoord: () -> Int = { if (hrzTurn) x else y }
    val collCoord: () -> Int = { if (hrzTurn) y else x }
    val getI: (i: Int, j: Int) -> Int = { i, j ->
        if (hrzTurn) xy2index(i, j, rowSize) else xy2index(j, i, rowSize)
    }

    // expanding first
    var delayAcc = ANIM_START_DELAY
    for (n in expCoord() until rowSize) {
        val delay = delayAcc
        delayAcc += SHIFT_DELAY * 2
        items.put(getI(n, collCoord()), createAnim(delay, hrzTurn))
    }
    delayAcc = ANIM_START_DELAY + SHIFT_DELAY
    for (n in expCoord() downTo 0) {
        val delay = delayAcc
        delayAcc += SHIFT_DELAY * 2
        items.put(getI(n, collCoord()), createAnim(delay, hrzTurn))
    }

    // collapsing
    var delayAcc2 = delayAcc
    for (n in rowSize - 1 downTo collCoord()) {
        val delay = delayAcc2
        delayAcc2 += SHIFT_DELAY * 2
        items.put(getI(expCoord(), n), createAnim(delay, hrzTurn.not()))
    }
    delayAcc2 = delayAcc + SHIFT_DELAY
    for (n in 0 until collCoord()) {
        val delay = delayAcc2
        delayAcc2 += SHIFT_DELAY * 2
        items.put(getI(expCoord(), n), createAnim(delay, hrzTurn.not()))
    }

    return items
}

private fun createAnim(
    delay: Long, isX: Boolean,
    durationMillis: Long = 300,
    changeVal: Float = dip(5).toFloat()
): Animator {
    val anim = ValueAnimator.ofFloat(changeVal / 2, changeVal, 0f).apply {
        duration = durationMillis
        startDelay = delay
    }
    return { v, callback ->
        anim.addUpdateListener { value ->
            val float = value.animatedValue as Float
            if (isX) v.translationX = float else v.translationY = float
            callback()
        }
        anim.start()
    }
}
