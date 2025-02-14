package com.example.imagedetectionapp.domain.repository

import com.example.imagedetectionapp.data.local.database.ImageDetectEntity
import com.example.imagedetectionapp.domain.model.ImageDetect
import com.example.imagedetectionapp.utils.GetResult
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getDetectImageList():Flow<GetResult<List<ImageDetect>>>

    suspend fun getDetectImageById(id:Int):Flow<GetResult<ImageDetect>>

    suspend fun addDetectImage(imageDetect: ImageDetect)
}