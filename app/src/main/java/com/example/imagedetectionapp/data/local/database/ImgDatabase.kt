package com.example.imagedetectionapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ImageDetectEntity::class], version = 1, exportSchema = false)
@TypeConverters(BitmapConverters::class)
abstract class ImgDatabase():RoomDatabase() {

    abstract fun dao():Dao
}