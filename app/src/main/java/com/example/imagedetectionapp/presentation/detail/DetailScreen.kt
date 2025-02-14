package com.example.imagedetectionapp.presentation.detail


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import java.io.File

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
  id:Int,
  navController: NavHostController
){
  val viewModel = hiltViewModel<DetailViewModel>()

  LaunchedEffect(id) {
    viewModel.getData(id)
  }
  val state = viewModel.detailState.collectAsState().value


  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.shadow(elevation = 5.dp),
        title = {
          Text(
            "Detail",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        },
        navigationIcon = {
          IconButton(onClick = {
            navController.popBackStack()
          }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Localized description"
            )
          }
        }
      )
    }
  ) {
    padding->
    Column(
      Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(padding).padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
    ) {
      Image(
        painter = rememberAsyncImagePainter(state.detailState?.image),
        contentDescription = "Detected Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp)
          .padding(top = 35.dp, bottom = 10.dp)
          .clip(RoundedCornerShape(15.dp))
      )

      Spacer(Modifier.height(10.dp))

      Text(
        "Result: ${state.detailState?.name?.uppercase()}",
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        modifier = Modifier
          .fillMaxWidth()
      )

      Text("Confidence score: ${String.format("%.2f", state.detailState?.score)}%",
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier
          .fillMaxWidth()
      )

      Text(
        text = "Detection time: ${state.detailState?.detectionTime?.toString() ?: "N/A"} ms",
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        modifier = Modifier
          .fillMaxWidth()
      )

      Spacer(Modifier.height(15.dp))

      HorizontalDivider(thickness = 1.dp)

      Spacer(Modifier.height(10.dp))

      state.detailState?.image?.let { imagePath ->
        val (rotatedBitmap, wasRotated) = processImage(imagePath)
        Text(
          text = if (wasRotated) "Image Rotated to Portrait" else "Image Already in Portrait",
          color = Color.Black,
          fontWeight = FontWeight.SemiBold,
          fontSize = 18.sp,
          modifier = Modifier.padding(bottom = 10.dp)
        )
        Image(
          painter = BitmapPainter(rotatedBitmap.asImageBitmap()),
          contentDescription = "Rotated Image",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clip(RoundedCornerShape(15.dp))
        )
      }

























    }

  }

}
fun processImage(imagePath: String): Pair<Bitmap, Boolean> {
  val bitmap = BitmapFactory.decodeFile(File(imagePath).absolutePath)
  val exif = ExifInterface(imagePath)
  val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

  val matrix = Matrix()
  var wasRotated = false

  when (orientation) {
    ExifInterface.ORIENTATION_ROTATE_90 -> {
      matrix.postRotate(90f)
      wasRotated = true
    }
    ExifInterface.ORIENTATION_ROTATE_180 -> {
      matrix.postRotate(180f)
      wasRotated = true
    }
    ExifInterface.ORIENTATION_ROTATE_270 -> {
      matrix.postRotate(270f)
      wasRotated = true
    }
  }


  if (!wasRotated && bitmap.width > bitmap.height) {
    matrix.postRotate(90f)
    wasRotated = true
  }

  val rotatedBitmap = if (wasRotated) {
    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
  } else {
    bitmap
  }

  return Pair(rotatedBitmap, wasRotated)
}



