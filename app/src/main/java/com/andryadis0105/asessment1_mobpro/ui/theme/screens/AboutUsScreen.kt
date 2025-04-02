package com.andryadis0105.asessment1_mobpro.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.andryadis0105.asessment1_mobpro.R

@Composable
fun AboutUsScreen( onBack: () -> Unit ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.about_us_image_foreground),
            contentDescription = "About Us",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aplikasi Jual Beli Beras Ponorogo",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Dibuat oleh Andry Adi S",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onBack() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Text("Kembali")
        }
    }
}