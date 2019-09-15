package com.livermor.plusminusme.model

import com.google.gson.annotations.SerializedName
import com.livermor.plusminusme.rand
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Board(
    val cells: List<Int>,
    @SerializedName("row-size") val rowSize: Int =
        sqrt(cells.count().toDouble()).roundToInt()
) {
    val count: Int = cells.count()

    operator fun get(i: Int): Int = cells[i]
}

fun Board.xy2index(x: Int, y: Int): Int = y * this.rowSize + x
fun Board.xy2index(xy: Pair<Int, Int>): Int = xy2index(xy.first, xy.second)
fun Board.get(x: Int, y: Int): Int = cells[xy2index(x, y)]
fun Board.coords(i: Int): Pair<Int, Int> = Pair(i % rowSize, i / rowSize)

fun generateBoard(rowSize: Int): Board {
    val cells = (0 until (rowSize * rowSize)).map { rand.nextInt(19) - 9 }
    return Board(cells.toPersistentList())
}

