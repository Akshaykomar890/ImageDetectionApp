package com.example.imagedetectionapp.data.repository


import android.util.Log
import com.example.imagedetectionapp.data.local.database.ImgDatabase
import com.example.imagedetectionapp.data.mapper.toImage
import com.example.imagedetectionapp.data.mapper.toImageEntity
import com.example.imagedetectionapp.domain.model.ImageDetect
import com.example.imagedetectionapp.domain.repository.Repository
import com.example.imagedetectionapp.utils.ErrorMessage
import com.example.imagedetectionapp.utils.GetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RepositoryImp @Inject constructor(
    private val database: ImgDatabase
):Repository
{
    override suspend fun getDetectImageList(): Flow<GetResult<List<ImageDetect>>> {
        val data = database.dao().getPredictionDataList()
            .map { list ->
                GetResult.Success(
                    list.map {
                        it.toImage()
                    }
                )
            }
            .catch { e ->
                Log.e("DatabaseError", "Error fetching data: ${e.message}")
            }
            .flowOn(Dispatchers.IO)
        return data
    }

    override suspend fun getDetectImageById(id: Int): Flow<GetResult<ImageDetect>> {
        return flow {
            try {
                val getList = database.dao().getPredictionById(id)
                emit(
                    GetResult.Success(
                        getList.toImage()
                    )
                )
            }catch (e:Exception){
                emit(
                    GetResult.Error(
                        ErrorMessage.DATABASE_ERROR
                    )
                )
            }
        }
    }

    override suspend fun addDetectImage(imageDetect: ImageDetect) {
        val entity = imageDetect.toImageEntity()

        database.dao().addPredictionData(entity)

        Log.d("DatabaseCheck", "Data inserted into database: $entity")
    }



}