package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {
    var rankingList by mutableStateOf<List<RankingDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadRanking() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.getRanking()
                if (response.isSuccessful) {
                    rankingList = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
