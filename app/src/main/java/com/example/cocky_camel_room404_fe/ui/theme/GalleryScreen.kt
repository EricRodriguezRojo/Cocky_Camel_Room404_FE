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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GalleryAlbum(
    val name: String,
    val coverImage: Int,
    val imageCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(onBack: () -> Unit) {
    val allPhotos = listOf(
        R.drawable.selfie1, R.drawable.selfie2,R.drawable.cuatro,
        R.drawable.selfie3,R.drawable.dos, R.drawable.foto1,
        R.drawable.foto2,R.drawable.nueve, R.drawable.foto3,
        R.drawable.foto4, R.drawable.foto5, R.drawable.foto6,
        R.drawable.siete, R.drawable.foto1, R.drawable.selfie1,
    )


    val albums = listOf(
        GalleryAlbum("Cámara", R.drawable.siete, 128),
        GalleryAlbum("Instagram", R.drawable.cuatro, 45),
        GalleryAlbum("WhatsApp", R.drawable.dos, 312),
        GalleryAlbum("Descargas", R.drawable.nueve, 12)
    )

    var selectedTab by remember { mutableStateOf(0) }
    var fullScreenImage by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopAppBar(
                title = { Text("Fotos", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color(0xFF007AFF)) // Azul iOS
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF007AFF),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF007AFF)
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Todas las fotos", color = if (selectedTab == 0) Color(0xFF007AFF) else Color.Gray) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Álbumes", color = if (selectedTab == 1) Color(0xFF007AFF) else Color.Gray) }
                )
            }

            if (selectedTab == 0) {
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
                                .clickable { fullScreenImage = photoRes },
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
                            modifier = Modifier.clickable { fullScreenImage = album.coverImage }
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
                            Text(text = "${album.imageCount} fotos", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        if (fullScreenImage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { fullScreenImage = null },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = fullScreenImage!!),
                    contentDescription = "Imagen en grande",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { fullScreenImage = null },
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Cerrar", tint = Color.White)
                }
            }
        }
    }
}