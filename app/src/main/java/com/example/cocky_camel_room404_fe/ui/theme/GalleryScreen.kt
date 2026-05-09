package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    onBack: () -> Unit,
    viewModel: GalleryViewModel = viewModel()
) {
    val allPhotos = listOf(
        R.drawable.selfie1, R.drawable.selfie2, R.drawable.cuatro,
        R.drawable.selfie3, R.drawable.dos, R.drawable.foto1,
        R.drawable.foto2, R.drawable.nueve, R.drawable.foto3,
        R.drawable.foto4, R.drawable.foto5, R.drawable.foto6,
        R.drawable.albora1, R.drawable.albora2, R.drawable.albora3,
    )

    val albums = listOf(
        GalleryAlbum(stringResource(R.string.gallery_camera), R.drawable.siete, 128),
        GalleryAlbum("Instagram", R.drawable.cuatro, 45),
        GalleryAlbum("WhatsApp", R.drawable.dos, 312),
        GalleryAlbum(stringResource(R.string.gallery_downloads), R.drawable.nueve, 12)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.gallery_title), color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color(0xFF007AFF))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            TabRow(
                selectedTabIndex = viewModel.selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF007AFF),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[viewModel.selectedTab]),
                        color = Color(0xFF007AFF)
                    )
                }
            ) {
                Tab(
                    selected = viewModel.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = { Text(stringResource(R.string.gallery_tab_all), color = if (viewModel.selectedTab == 0) Color(0xFF007AFF) else Color.Gray) }
                )
                Tab(
                    selected = viewModel.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = { Text(stringResource(R.string.gallery_tab_albums), color = if (viewModel.selectedTab == 1) Color(0xFF007AFF) else Color.Gray) }
                )
            }

            if (viewModel.selectedTab == 0) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(allPhotos) { photoRes ->
                        Image(
                            painter = painterResource(id = photoRes),
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { viewModel.onImageClick(photoRes) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(albums) { album ->
                        Column(
                            modifier = Modifier.clickable { viewModel.onImageClick(album.coverImage) }
                        ) {
                            Image(
                                painter = painterResource(id = album.coverImage),
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = album.name, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                            Text(text = "${album.imageCount} ${stringResource(R.string.gallery_photos_count)}", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        if (viewModel.fullScreenImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { viewModel.dismissFullScreen() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = viewModel.fullScreenImage!!),
                    contentDescription = stringResource(R.string.gallery_full_screen_desc),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { viewModel.dismissFullScreen() },
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.close), tint = Color.White)
                }
            }
        }
    }
}
