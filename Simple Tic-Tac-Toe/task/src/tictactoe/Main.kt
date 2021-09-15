package tictactoe

import kotlin.math.abs


object Board {

    const val horizontalBorder = "---------"
    const val verticalBorder = '|'

    private const val boardSize = 3
    var currentTurn = Element.X

    val list = mutableListOf(
        mutableListOf(Element.EMPTY, Element.EMPTY, Element.EMPTY),
        mutableListOf(Element.EMPTY, Element.EMPTY, Element.EMPTY),
        mutableListOf(Element.EMPTY, Element.EMPTY, Element.EMPTY)
    )

    fun changeTurn() {
        currentTurn = when (currentTurn) {
            Element.X -> Element.O
            Element.O -> Element.X
            else -> Element.EMPTY
        }
    }

    fun setBoard(str: String) {
        for (j in 0..2) {
            for (i in 0..2) {
                list[j][i] = getByName(str[3 * j + i].toString())
            }
        }
    }

    fun getState(): State {
        val isWonX = (checkRows(Element.X) || checkColumns(Element.X) || checkDiagonals(Element.X))
        val isWonO = (checkRows(Element.O) || checkColumns(Element.O) || checkDiagonals(Element.O))
        return when {
            isWonX && isWonO || getDifference() > 1 -> State.IMPOSSIBLE
            isWonX -> State.X_WINS
            isWonO -> State.O_WINS
            getCount() == 9 -> State.DRAW
            else -> State.NOT_FINISHED
        }
    }

    private fun getByName(name: String): Element {
        for (i in Element.values()) {
            if (name == i.name) return i
        }
        return Element.EMPTY
    }

    private fun checkRows(element: Element): Boolean {
        for (i in 0 until boardSize) {
            if (list[i][0] == element && list[i][1] == element && list[i][2] == element) {
                return true
            }
        }
        return false
    }

    private fun checkColumns(element: Element): Boolean {
        for (i in 0 until boardSize) {
            if (list[0][i] == element && list[1][i] == element && list[2][i] == element) {
                return true
            }
        }
        return false
    }

    private fun checkDiagonals(element: Element): Boolean {
        if (list[0][0] == element && list[1][1] == element && list[2][2] == element ||
            list[2][0] == element && list[1][1] == element && list[0][2] == element
        ) {
            return true
        }
        return false
    }

    private fun getCount(): Int {
        var cnt = 0
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (list[i][j] != Element.EMPTY) {
                    cnt++
                }
            }
        }
        return cnt
    }

    private fun getDifference(): Int {
        var xes = 0
        var oes = 0
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (list[i][j] == Element.X) {
                    xes++
                }
                if (list[i][j] == Element.O) {
                    oes++
                }
            }
        }
        return abs(xes - oes)
    }

    enum class Element(val character: String) {
        X("X"),
        O("O"),
        EMPTY("_");
    }

    enum class State(val value: String) {
        NOT_FINISHED("Game not finished"),
        DRAW("Draw"),
        X_WINS("X wins"),
        O_WINS("O wins"),
        IMPOSSIBLE("Impossible")
    }
}

fun main() {
    printBoard()
    while (!(Board.getState() == Board.State.DRAW ||
                Board.getState() == Board.State.X_WINS ||
                Board.getState() == Board.State.O_WINS)
    ) {
        makeMove()
        Board.changeTurn()
    }

    println(Board.getState().value)


}

fun makeMove() {
    while (true) {
        print("Enter the coordinates: ")
        val (xx, yy) = readLine()!!.toString().split(" ")
        if (checkStrings(xx, yy)) {
            val x = xx.toInt()
            val y = yy.toInt()
            if (x in 1..3 && y in 1..3) {
                if (Board.list[x - 1][y - 1] == Board.Element.EMPTY) {
                    Board.list[x - 1][y - 1] = Board.currentTurn
                    printBoard()
                    return
                } else {
                    println("This cell is occupied! Choose another one!")
                }
            } else {
                println("Coordinates should be from 1 to 3!")
            }
        } else {
            println("You should enter numbers!")
        }
    }

}

fun checkStrings(x: String, y: String): Boolean {
    return if (x.length == 1 || y.length == 1) {
        val isCorrectFirst = x.first().isDigit()
        val isCorrectSecond = y.first().isDigit()
        isCorrectFirst && isCorrectSecond
    } else {
        false
    }

}

fun printBoard() {
    println(Board.horizontalBorder)
    for (i in 0..2) {
        println("${Board.verticalBorder} ${Board.list[i].joinToString(" ") { it.character }} ${Board.verticalBorder}")
    }
    println(Board.horizontalBorder)
}