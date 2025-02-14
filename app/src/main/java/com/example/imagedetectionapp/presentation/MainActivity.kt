package com.example.imagedetectionapp.presentation

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.compose.ImageDetectionAppTheme
import com.example.imagedetectionapp.destination.Detail
import com.example.imagedetectionapp.destination.Home
import com.example.imagedetectionapp.presentation.detail.DetailScreen
import com.example.imagedetectionapp.presentation.home.HomeScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageDetectionAppTheme {
                SystemBarColor(color = MaterialTheme.colorScheme.inverseSurface)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text("", modifier = Modifier.padding(innerPadding))
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Home){
                        composable<Home> {
                            HomeScreen(
                                navController = navController
                            )
                        }
                        composable<Detail> {
                            val route = it.toRoute<Detail>()
                            DetailScreen(
                                route.id,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SystemBarColor(color: Color){
        val ui = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            ui.setSystemBarsColor(color)
        }
    }
}

