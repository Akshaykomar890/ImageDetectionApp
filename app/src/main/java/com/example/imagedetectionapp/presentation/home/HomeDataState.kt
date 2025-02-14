package com.example.imagedetectionapp.presentation.home

import com.example.imagedetectionapp.domain.model.ImageDetect
import com.example.imagedetectionapp.utils.ErrorMessage

data class HomeDataState(
    val detectList: List<ImageDetect> = emptyList(),
    val errorMessage: String? = null

)
