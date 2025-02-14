package com.example.imagedetectionapp.data.local.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_detect_entity")
data class ImageDetectEntity(
    val name:String,
    val image:String,
    val score:Float,

    val wasRotated: Boolean = false,

    val detectionTime: Long?,

    @PrimaryKey(autoGenerate = true)
    val id:Int = 0
)
