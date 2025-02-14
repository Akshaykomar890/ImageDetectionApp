package com.example.imagedetectionapp.di


import android.content.Context
import androidx.room.Room
import com.example.imagedetectionapp.data.local.database.BitmapConverters
import com.example.imagedetectionapp.data.local.database.ImgDatabase
import com.example.imagedetectionapp.data.repository.RepositoryImp
import com.example.imagedetectionapp.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBitmapConverter(): BitmapConverters {
        return BitmapConverters()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): ImgDatabase {
        return Room.databaseBuilder(
            app,
            ImgDatabase::class.java,
            "image_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageClassifierOptions(): ImageClassifier.ImageClassifierOptions {
        return ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(3)
            .build()
    }


    @Provides
    @Singleton
    fun provideImageClassifier(
        @ApplicationContext context: Context,
        options: ImageClassifier.ImageClassifierOptions
    ): ImageClassifier {
        return ImageClassifier.createFromFileAndOptions(
            context, "2.tflite", options
        )
    }


    @Singleton
    @Provides
    fun provideRepositoryImp(database: ImgDatabase): Repository {
        return RepositoryImp(
            database
        )
    }
}


