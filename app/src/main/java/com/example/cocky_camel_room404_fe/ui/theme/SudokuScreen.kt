package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(onBack: () -> Unit) {
    val initialBoard = remember { listOf(
        listOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
        listOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
        listOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
        listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
        listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
        listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
        listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
        listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
        listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
    )}

    val solvedBoard = remember { listOf(
        listOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
        listOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
        listOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
        listOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
        listOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
        listOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
        listOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
        listOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
        listOf(3, 4, 5, 2, 8, 6, 1, 7, 9)
    )}

    var board by remember { mutableStateOf(initialBoard.map { it.toMutableList() }) }
    var selectedCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var hintsRemaining by remember { mutableStateOf(3) }
    var showDialog by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    fun validateBoard() {
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

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D0D))) {
        TopAppBar(
            title = { Text(stringResource(R.string.app_sudoku), color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.sudoku_hints_label)} $hintsRemaining",
                color = if (hintsRemaining > 0) Color.LightGray else Color.Red,
                fontSize = 16.sp
            )
            Button(
                onClick = {
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
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00), contentColor = Color.White),
                enabled = hintsRemaining > 0 && selectedCell != null
            ) {
                Icon(Icons.Filled.Info, null, tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.sudoku_hint_button))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(1f)
                .border(2.dp, Color.White)
                .background(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                for (row in 0 until 9) {
                    Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        for (col in 0 until 9) {
                            val isInitial = initialBoard[row][col] != 0
                            val isSelected = selectedCell == Pair(row, col)
                            val value = board[row][col]
                            val isWrong = !isInitial && value != 0 && value != solvedBoard[row][col]

                            Box(
                                modifier = Modifier
                                    .weight(1f).fillMaxHeight().border(0.5.dp, Color.DarkGray)
                                    .background(if (isSelected) Color(0xFF3A3A3A) else Color.Transparent)
                                    .clickable { if (!isInitial) selectedCell = Pair(row, col) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (value != 0) {
                                    Text(
                                        text = value.toString(),
                                        color = when {
                                            isInitial -> Color.White
                                            isWrong -> Color(0xFFCF6679)
                                            else -> Color(0xFF2196F3)
                                        },
                                        fontSize = 22.sp,
                                        fontWeight = if (isInitial) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(3f))
                Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(Color.White))
                Spacer(modifier = Modifier.weight(3f))
                Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(Color.White))
                Spacer(modifier = Modifier.weight(3f))
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(3f))
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White))
                Spacer(modifier = Modifier.weight(3f))
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White))
                Spacer(modifier = Modifier.weight(3f))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val rows = listOf(1..5, 6..9)
            for (range in rows) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (num in range) {
                        Button(
                            onClick = {
                                selectedCell?.let { (r, c) ->
                                    val newBoard = board.map { it.toMutableList() }.toMutableList()
                                    newBoard[r][c] = num
                                    board = newBoard
                                    validateBoard()
                                }
                            },
                            modifier = Modifier.weight(1f).aspectRatio(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2A2A2A),
                                contentColor = Color.White
                            )
                        ) {
                            Text(num.toString(), fontSize = 22.sp)
                        }
                    }
                    if (range == 6..9) {
                        Button(
                            onClick = {
                                selectedCell?.let { (r, c) ->
                                    val newBoard = board.map { it.toMutableList() }.toMutableList()
                                    newBoard[r][c] = 0
                                    board = newBoard
                                }
                            },
                            modifier = Modifier.weight(1f).aspectRatio(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFCF6679),
                                contentColor = Color.White
                            )
                        ) {
                            Text("X", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (isSuccess) stringResource(R.string.sudoku_success_title) else stringResource(R.string.sudoku_error_title),
                    color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFCF6679)
                )
            },
            text = {
                Text(
                    text = if (isSuccess) stringResource(R.string.sudoku_success_text) else stringResource(R.string.sudoku_error_text),
                    color = Color.White
                )
            },
            confirmButton = {
                Button(onClick = { showDialog = false; if (isSuccess) onBack() }) {
                    Text(stringResource(R.string.accept))
                }
            },
            containerColor = Color(0xFF1E1E1E)
        )
    }
}