package com.andryadis0105.asessment1_mobpro.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DistanceConverter() {
    val inputText  = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }
    val showWarning  = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputText.value,
            onValueChange = {
                inputText.value = it
                showWarning.value = it.isEmpty()
            },
            label = { Text("Masukkan jarak (meter)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (showWarning.value) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Harap masukkan angka!", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (inputText.value.isNotEmpty()) {
                result.value = "${inputText.value.toFloat() / 1000} km"
                showWarning.value = false
            } else {
                showWarning.value = true
            }
        }) {
            Text("Konversi")
        }

        if (result.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hasil: ${result.value}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}