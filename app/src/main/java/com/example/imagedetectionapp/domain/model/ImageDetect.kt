package com.example.imagedetectionapp.domain.model



data class ImageDetect(
    val name:String,
    val image:String,
    val score:Float,
    val wasRotated: Boolean = false,
    val detectionTime: Long?,
    val id:Int
)
