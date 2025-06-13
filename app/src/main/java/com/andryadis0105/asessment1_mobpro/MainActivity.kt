package com.andryadis0105.asessment1_mobpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.andryadis0105.asessment1_mobpro.ui.screen.MainScreen
import com.andryadis0105.asessment1_mobpro.ui.theme.Asessment1_MobproTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Asessment1_MobproTheme {
                MainScreen()
            }
        }
    }
}