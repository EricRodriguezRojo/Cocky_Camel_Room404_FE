package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Note(val title: String, val content: String, val date: String)

class NotesViewModel : ViewModel() {
    var notes by mutableStateOf<List<Note>>(emptyList())
        private set

    fun loadNotes(
        shoppingTitle: String, shoppingContent: String, shoppingDate: String,
        tfgTitle: String, tfgContent: String, tfgDate: String,
        dontDeleteTitle: String, dontDeleteContent: String,
        gymTitle: String, gymContent: String,
        passwordsTitle: String, passwordsContent: String
    ) {
        notes = listOf(
            Note(title = shoppingTitle, content = shoppingContent, date = shoppingDate),
            Note(title = tfgTitle, content = tfgContent, date = tfgDate),
            Note(title = dontDeleteTitle, content = dontDeleteContent, date = "10 May, 23:15"),
            Note(title = gymTitle, content = gymContent, date = "01 May, 08:00"),
            Note(title = passwordsTitle, content = passwordsContent, date = "15 Abr, 12:45")
        )
    }
}
