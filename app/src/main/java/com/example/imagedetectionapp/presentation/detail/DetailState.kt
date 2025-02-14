package com.example.imagedetectionapp.presentation.detail

import com.example.imagedetectionapp.domain.model.ImageDetect

data class DetailState(
    val detailState:ImageDetect? = null,
    val errorMsg:String? = null
)
