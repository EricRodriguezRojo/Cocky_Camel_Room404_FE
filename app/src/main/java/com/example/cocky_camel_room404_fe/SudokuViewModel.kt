package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SudokuViewModel : ViewModel() {
    val initialBoard = listOf(
        listOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
        listOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
        listOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
        listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
        listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
        listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
        listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
        listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
        listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
    )

    private val solvedBoard = listOf(
        listOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
        listOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
        listOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
        listOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
        listOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
        listOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
        listOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
        listOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
        listOf(3, 4, 5, 2, 8, 6, 1, 7, 9)
    )

    var board by mutableStateOf(initialBoard.map { it.toMutableList() })
        private set

    var selectedCell by mutableStateOf<Pair<Int, Int>?>(null)
        private set

    var hintsRemaining by mutableIntStateOf(3)
        private set

    var showDialog by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
        private set

    fun onCellClick(row: Int, col: Int) {
        if (initialBoard[row][col] == 0) {
            selectedCell = Pair(row, col)
        }
    }

    fun onNumberInput(num: Int) {
        selectedCell?.let { (r, c) ->
            val newBoard = board.map { it.toMutableList() }.toMutableList()
            newBoard[r][c] = num
            board = newBoard
            validateBoard()
        }
    }

    fun onClearCell() {
        selectedCell?.let { (r, c) ->
            val newBoard = board.map { it.toMutableList() }.toMutableList()
            newBoard[r][c] = 0
            board = newBoard
        }
    }

    fun useHint() {
        if (hintsRemaining > 0 && selectedCell != null) {
            val (r, c) = selectedCell!!
            if (initialBoard[r][c] == 0 && board[r][c] != solvedBoard[r][c]) {
                val newBoard = board.map { it.toMutableList() }.toMutableList()
                newBoard[r][c] = solvedBoard[r][c]
                board = newBoard
                hintsRemaining--
                validateBoard()
            }
        }
    }

    private fun validateBoard() {
        var isFull = true
        var isCorrect = true
        for (r in 0..8) {
            for (c in 0..8) {
                if (board[r][c] == 0) isFull = false
                if (board[r][c] != 0 && board[r][c] != solvedBoard[r][c]) isCorrect = false
            }
        }
        if (isFull) {
            isSuccess = isCorrect
            showDialog = true
        }
    }
    
    fun getSolvedValue(row: Int, col: Int): Int {
        return solvedBoard[row][col]
    }

    fun dismissDialog() {
        showDialog = false
    }
}
