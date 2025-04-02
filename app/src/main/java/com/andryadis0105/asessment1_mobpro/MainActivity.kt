package com.andryadis0105.asessment1_mobpro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andryadis0105.asessment1_mobpro.ui.theme.Asessment1_MobproTheme
import androidx.compose.runtime.saveable.rememberSaveable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Asessment1_MobproTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("about") { AboutUsScreen(navController) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(title: String, navController: NavController?, showBack: Boolean = false) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { navController?.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            IconButton(onClick = { navController?.navigate("about") }) {
                Icon(Icons.Default.Info, contentDescription = "About Us")
            }
        }
    )
}

@Composable
fun MainScreen(navController: NavController) {
    var inputText by rememberSaveable { mutableStateOf("") }
    var convertedValue by rememberSaveable { mutableStateOf("") }
    var showWarning by rememberSaveable { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedConversion by rememberSaveable { mutableStateOf("Meter ke Kilometer") }

    val conversionOptions = listOf("Meter ke Kilometer", "Kilometer ke Meter")

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { CustomTopBar(title = " Menghitung M ke Km/ KM ke M", navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Menghitung Jarak", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = inputText,
                onValueChange = { inputText = it; showWarning = false },
                label = { Text("Masukkan Nilai") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text("Pilih dan Klik terlebih dahulu:")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = true }) {
                    Text(selectedConversion)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    conversionOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedConversion = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                convertedValue = when {
                    inputText.isEmpty() -> {
                        showWarning = true
                        "Harap isi nilai!"
                    }
                    inputText.toDoubleOrNull() == null -> {
                        showWarning = true
                        "Input tidak valid!"
                    }
                    else -> {
                        val value = inputText.toDouble()
                        if (selectedConversion == "Meter ke Kilometer") "${value / 1000} km" else "${value * 1000} m"
                    }
                }
            }) {
                Text("Hasil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (convertedValue.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showWarning) {
                        Icon(Icons.Filled.Warning, contentDescription = "Warning", tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = "Hasil: $convertedValue",
                        fontSize = 18.sp,
                        color = if (showWarning) Color.Red else Color.Blue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                navController.context.startActivity(intent)
            }) {
                Text("Kunjungi Website")
            }
        }
    }
}

@Composable
fun AboutUsScreen(navController: NavController) {
    Scaffold(
        topBar = { CustomTopBar(title = "About Us", navController = navController, showBack = true) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.about_us_image_foreground),
                contentDescription = "About Us",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Aplikasi ini dikembangkan untuk membantu jual beli beras di Ponorogo.")
        }
    }
}
