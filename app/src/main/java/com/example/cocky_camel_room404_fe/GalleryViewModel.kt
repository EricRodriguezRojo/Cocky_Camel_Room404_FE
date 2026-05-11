package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class GalleryAlbum(
    val name: String,
    val coverImage: Int,
    val imageCount: Int
)

class GalleryViewModel : ViewModel() {
    var selectedTab by mutableStateOf(0)
    var fullScreenImage by mutableStateOf<Int?>(null)

    fun onTabSelected(index: Int) {
        selectedTab = index
    }

    fun onImageClick(imageRes: Int) {
        fullScreenImage = imageRes
    }

    fun dismissFullScreen() {
        fullScreenImage = null
    }
}
