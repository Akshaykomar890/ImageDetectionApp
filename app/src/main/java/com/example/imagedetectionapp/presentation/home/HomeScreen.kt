package com.example.imagedetectionapp.presentation.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.imagedetectionapp.presentation.component.HomeItem
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val context = LocalContext.current

    var hasCameraPermission by remember { mutableStateOf(false) }

    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var selectedImage by  remember {
        mutableStateOf<Bitmap?>(null)
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val cameraImageUri = remember {
        mutableStateOf(
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                File(context.cacheDir, "captured_image.jpg")
            )
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            selectedImage = bitmap
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri = cameraImageUri.value
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            selectedImage = bitmap
        }
    }
    LaunchedEffect(
        selectedImage
    ) {
        selectedImage?.let { viewModel.detectImage(context,it) }
    }


    val state = viewModel.detectState.collectAsState().value

    Column(
        modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.shadow(elevation = 5.dp),
                    title = {
                        Text("Image AI", fontWeight = FontWeight.Normal, fontSize = 24.sp )
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            }

        ) {
            padding->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                LazyColumn(
                ) {
                    items(
                        count = state.detectList.size
                    ){
                            it->
                        HomeItem(
                            state.detectList[it],
                            navController,
                            id  = state.detectList[it].id
                        )
                    }

                }
//                Button(
//                    onClick = {
//                        deleteDatabase(context)
//                    }
//                ) {
//                    Text("Clear")
//                }
                FloatingActionButton(
                    onClick = {
                        showImagePickerDialog(
                            context = context,
                            onGalleryClick = { galleryLauncher.launch("image/*") },
                            onCameraClick = { cameraLauncher.launch(cameraImageUri.value) }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 24.dp)
                        .size(60.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Open Gallery")
                }
            }
        }
    }
}

fun showImagePickerDialog(
    context: Context,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle("Choose an Option")
        .setItems(arrayOf("Camera", "Gallery")) { _, which ->
            when (which) {
                0 -> onCameraClick()
                1 -> onGalleryClick()
            }
        }
        .show()
}
fun deleteDatabase(context: Context) {
    context.deleteDatabase("image_database") // Use the exact database name
}


