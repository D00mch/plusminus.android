package com.livermor.plusminus.model


import com.google.gson.annotations.SerializedName
import com.livermor.plusminus.rand
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList


data class State(
    val board: Board = generateBoard(5),
    val start: Int = 0,
    @SerializedName("hrz-points") val hrzPoints: Int,
    @SerializedName("vrt-points") val vrtPoints: Int,
    @SerializedName("hrz-turn") val isHrzTurn: Boolean,
    val moves: List<Int> = persistentListOf()
) {

    val validMoves: List<Int> = let {
        val (x, y) = board.coords(moves.lastOrNull() ?: start)
        (0 until board.rowSize)
            .map { if (isHrzTurn) Pair(it, y) else Pair(x, it) }
            .map(board::xy2index)
            .filterNot(moves::contains)
    }
}

fun generateState(rowSize: Int): State = State(
    generateBoard(rowSize),
    start = rand.nextInt(rowSize),
    hrzPoints = 0, vrtPoints = 0, isHrzTurn = rand.nextBoolean()
)

fun State.rowsCount(): Int = board.rowSize
fun State.size(): Int = board.count
fun State.isStart(): Boolean = moves.isEmpty()

fun State.isValidMove(i: Int): Boolean = validMoves.contains(i)
fun State.isValidMove(x: Int, y: Int): Boolean = isValidMove(xy2index(x, y, rowsCount()))

fun State.move(i: Int): State = copy(
    moves = moves.toPersistentList().add(i),
    isHrzTurn = isHrzTurn.not(),
    hrzPoints = if (isHrzTurn) hrzPoints + board[i] else hrzPoints,
    vrtPoints = if (isHrzTurn) vrtPoints else vrtPoints + board[i]
)

fun State.move(x: Int, y: Int): State = move(xy2index(x, y, rowsCount()))

fun State.moveVal(i: Int): Int = board[i]
fun State.anyMoves(): Boolean = validMoves.isNotEmpty()

fun State.print() {
    println("h: $hrzPoints ${if (isHrzTurn) "<- turn" else ""}")
    println("h: $vrtPoints ${if (isHrzTurn) "" else "<- turn"}")
    println("moves: ${moves.map(board::coords)}")
    for (i: Int in (0 until board.count)) {
        val (x, y) = board.coords(i)
        val isColored = isValidMove(i)
        val isNewLine = (x + 1) % rowsCount() == 0
        val isHidden = moves.contains(i)
        val value = board.cells[i].let { c ->
            when {
                isHidden -> " *";
                (c < 0) -> c
                else -> " $c"
            }
        }
        print("$value|")
        if (isNewLine) println()
    }
}
