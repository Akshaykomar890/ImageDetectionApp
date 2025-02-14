package com.example.imagedetectionapp.utils

sealed class GetResult<T>(
     val data:T? = null,
     val errorMessage:ErrorMessage? = null
) {
    class Success<T>(data: T):GetResult<T>(data = data)

    class Error<T>(errorMessage: ErrorMessage,data:T? = null):GetResult<T>(data = data, errorMessage = errorMessage)

}

enum class ErrorMessage{
    MODEL_LOAD_FAILED,
    CLASSIFICATION_FAILED,
    INVALID_IMAGE_FORMAT,
    DATABASE_ERROR,
    STORAGE_PERMISSION_DENIED,
    CAMERA_ACCESS_FAILED,
    FILE_READ_ERROR,
    UNKNOWN_ERROR
}