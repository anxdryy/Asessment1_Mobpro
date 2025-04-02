package com.andryadis0105.asessment1_mobpro.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.andryadis0105.asessment1_mobpro.ui.theme.components.CustomTopBar
import com.andryadis0105.asessment1_mobpro.ui.theme.components.DistanceConverter

@Composable
fun MainScreen() {
    val currentScreen  = remember { mutableStateOf("Home") }

    Scaffold(
        topBar = {
            CustomTopBar(title = "Jual Beli Beras Ponorogo") { menuItem ->
                if (menuItem == "AboutUs") {
                    currentScreen.value = "AboutUs"
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (currentScreen.value) {
                "Home" -> DistanceConverter()
                "AboutUs" -> AboutUsScreen { currentScreen.value = "Home" }
            }
        }
    }
}