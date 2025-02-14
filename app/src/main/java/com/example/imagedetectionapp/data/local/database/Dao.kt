package com.example.imagedetectionapp.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM image_detect_entity")
    fun getPredictionDataList():Flow<List<ImageDetectEntity>>

    @Upsert
    suspend fun addPredictionData (
        predictionData: ImageDetectEntity
    )

    @Query("SELECT * FROM image_detect_entity WHERE ID = :id")
    suspend fun getPredictionById(
        id:Int
    ): ImageDetectEntity

}