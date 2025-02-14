package com.example.imagedetectionapp.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.icu.util.TimeZone.SystemTimeZoneType
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagedetectionapp.data.local.database.BitmapConverters
import com.example.imagedetectionapp.domain.model.ImageDetect
import com.example.imagedetectionapp.domain.repository.Repository
import com.example.imagedetectionapp.utils.ErrorMessage
import com.example.imagedetectionapp.utils.GetResult
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val imageClassifier: ImageClassifier
):ViewModel() {

    private val _detectState = MutableStateFlow(HomeDataState())
    val detectState = _detectState.asStateFlow()

    init {
        loadData()
    }



    fun detectImage(context: Context,image:Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()

            val convertImage = TensorImage.fromBitmap(image)
            val imageList = imageClassifier.classify(convertImage)

            val topCategory = imageList.firstOrNull()?.categories?.maxByOrNull { it.score }
            val label = topCategory?.label ?: "Unknown"
            val score: Float = (topCategory?.score ?: 0.0f) * 100f

            val imagePath = saveImageToInternalStorage(context, image)

            val endTime = System.currentTimeMillis()
            val detectionTime = endTime - startTime

            logDetectionTimeToFirebase(detectionTime)





            repository.addDetectImage(
                ImageDetect(
                    name = label,
                    image = imagePath,
                    score = score,
                    detectionTime = detectionTime,
                    id = 0
                )
            )
        }
    }

    private fun loadData(){
        viewModelScope.launch {
            repository.getDetectImageList().collectLatest { result ->
                when (result) {
                    is GetResult.Success -> {
                        result.data?.let { data ->
                            _detectState.update {
                                it.copy(detectList = data)
                            }
                        }
                    }

                    is GetResult.Error -> {
                        result.errorMessage?.let { error ->
                            val errorMsg = when (error) {
                                ErrorMessage.MODEL_LOAD_FAILED -> "Model failed to load. Try again."
                                ErrorMessage.CLASSIFICATION_FAILED -> "Image classification failed."
                                ErrorMessage.INVALID_IMAGE_FORMAT -> "Unsupported image format."
                                ErrorMessage.DATABASE_ERROR -> "Database error. Please retry."
                                ErrorMessage.STORAGE_PERMISSION_DENIED -> "Storage permission denied."
                                ErrorMessage.CAMERA_ACCESS_FAILED -> "Unable to access the camera."
                                ErrorMessage.FILE_READ_ERROR -> "Error reading the file."
                                ErrorMessage.UNKNOWN_ERROR -> "An unknown error occurred."
                            }
                            _detectState.update {
                                it.copy(errorMessage = errorMsg)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String {
        val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
        return file.absolutePath
    }

    private fun logDetectionTimeToFirebase(detectionTime: Long) {
        val firebaseAnalytics = Firebase.analytics

        val bundle = Bundle().apply {
            putLong("detection_time_ms", detectionTime)
        }
        firebaseAnalytics.logEvent("image_detection_time", bundle)

        Log.d("FirebaseAnalyticsDebug", "Logged event: image_detection_time with detection time $detectionTime ms")

    }
}