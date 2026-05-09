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
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(
    onBack: () -> Unit,
    viewModel: SudokuViewModel = viewModel()
) {
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
                text = "${stringResource(R.string.sudoku_hints_label)} ${viewModel.hintsRemaining}",
                color = if (viewModel.hintsRemaining > 0) Color.LightGray else Color.Red,
                fontSize = 16.sp
            )
            Button(
                onClick = { viewModel.useHint() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00), contentColor = Color.White),
                enabled = viewModel.hintsRemaining > 0 && viewModel.selectedCell != null
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
                            val isInitial = viewModel.initialBoard[row][col] != 0
                            val isSelected = viewModel.selectedCell == Pair(row, col)
                            val value = viewModel.board[row][col]
                            
                            val solvedValue = viewModel.getSolvedValue(row, col)
                            val isWrong = !isInitial && value != 0 && value != solvedValue

                            Box(
                                modifier = Modifier
                                    .weight(1f).fillMaxHeight().border(0.5.dp, Color.DarkGray)
                                    .background(if (isSelected) Color(0xFF3A3A3A) else Color.Transparent)
                                    .clickable { viewModel.onCellClick(row, col) },
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
                            onClick = { viewModel.onNumberInput(num) },
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
                            onClick = { viewModel.onClearCell() },
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

    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = {
                Text(
                    text = if (viewModel.isSuccess) stringResource(R.string.sudoku_success_title) else stringResource(R.string.sudoku_error_title),
                    color = if (viewModel.isSuccess) Color(0xFF4CAF50) else Color(0xFFCF6679)
                )
            },
            text = {
                Text(
                    text = if (viewModel.isSuccess) stringResource(R.string.sudoku_success_text) else stringResource(R.string.sudoku_error_text),
                    color = Color.White
                )
            },
            confirmButton = {
                Button(onClick = { 
                    viewModel.dismissDialog()
                    if (viewModel.isSuccess) onBack() 
                }) {
                    Text(stringResource(R.string.accept))
                }
            },
            containerColor = Color(0xFF1E1E1E)
        )
    }
}
