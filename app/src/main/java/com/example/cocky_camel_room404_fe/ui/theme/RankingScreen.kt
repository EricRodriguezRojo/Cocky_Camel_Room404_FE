package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    onBack: () -> Unit,
    viewModel: RankingViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadRanking()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_sistema),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f))) {
            TopAppBar(
                title = { Text(stringResource(R.string.ranking_global_leaderboard), color = Color.White, letterSpacing = 2.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Text("#", Modifier.width(30.dp), color = Color.Gray)
                            Text(stringResource(R.string.ranking_column_user), Modifier.weight(1f), color = Color.Gray)
                            Text(stringResource(R.string.ranking_column_pts), Modifier.width(60.dp), color = Color.Gray)
                            Text(stringResource(R.string.ranking_column_time), Modifier.width(60.dp), color = Color.Gray)
                        }
                    }

                    itemsIndexed(viewModel.rankingList) { index, entry ->
                        Surface(
                            color = Color.White.copy(alpha = 0.05f),
                            border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f)),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${index + 1}", Modifier.width(30.dp), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(entry.nickname ?: stringResource(R.string.ranking_anon), Modifier.weight(1f), color = Color.White)
                                Text("${entry.totalPoints}", Modifier.width(60.dp), color = Color.White)
                                Text("${entry.totalTime}s", Modifier.width(60.dp), color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
