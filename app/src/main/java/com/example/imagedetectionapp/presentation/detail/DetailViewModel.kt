package com.example.imagedetectionapp.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagedetectionapp.domain.repository.Repository
import com.example.imagedetectionapp.utils.ErrorMessage
import com.example.imagedetectionapp.utils.GetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()


    fun getData(id:Int){
        viewModelScope.launch {
            repository.getDetectImageById(id).collectLatest {
                collect->
                when(collect){
                    is GetResult.Success -> {
                        collect.data?.let {
                            data->
                            _detailState.update {
                                it.copy(detailState = data)
                            }
                        }
                    }
                    is GetResult.Error -> {
                        collect.errorMessage?.let { error ->
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
                            _detailState.update {
                                it.copy(errorMsg = errorMsg)
                            }
                        }
                    }
                }
            }
        }
    }
}