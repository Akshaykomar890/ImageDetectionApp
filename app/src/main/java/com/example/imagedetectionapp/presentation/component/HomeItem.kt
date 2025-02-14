package com.example.imagedetectionapp.presentation.component


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.imagedetectionapp.destination.Detail
import com.example.imagedetectionapp.domain.model.ImageDetect


@SuppressLint("DefaultLocale")
@Composable
fun HomeItem(
    item:ImageDetect,
    navController: NavHostController,
    id:Int
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(start = 25.dp, end = 25.dp, top = 35.dp, bottom = 10.dp)
            .shadow(3.dp, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                    navController.navigate(Detail(item.id))
            }
    ){

        Column(
            Modifier.fillMaxSize()
                .padding(bottom = 5.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(item.image),
                contentDescription = "Detected Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(
                "Result: ${item.name.uppercase()}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, top = 16.dp)
            )

            Text("Confidence Score: ${String.format("%.2f", item.score)}%",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth().padding(start = 16.dp, top = 16.dp)
            )







        }

    }

}