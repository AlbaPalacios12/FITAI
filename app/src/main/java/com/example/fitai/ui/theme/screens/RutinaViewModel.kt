package com.example.fitai.ui.theme.screens
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class RutinaViewModel : ViewModel() {
    val estadoHecho = mutableStateMapOf<String, Boolean>()
}
