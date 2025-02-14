package com.example.imagedetectionapp.data.mapper

import com.example.imagedetectionapp.data.local.database.ImageDetectEntity
import com.example.imagedetectionapp.domain.model.ImageDetect

fun ImageDetectEntity.toImage():ImageDetect{
    return ImageDetect(
        name = name,
        image = image,
        score = score,
        wasRotated = wasRotated,
        detectionTime = detectionTime,
        id = id
    )
}

fun ImageDetect.toImageEntity():ImageDetectEntity{
    return ImageDetectEntity(
        name = name,
        image = image,
        score = score,
        wasRotated = wasRotated,
        detectionTime = detectionTime
    )
}