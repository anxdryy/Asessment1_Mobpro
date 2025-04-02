package com.andryadis0105.asessment1_mobpro.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(title: String, onMenuItemClick: (String) -> Unit) {
    val expanded  = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title) },
        navigationIcon = {
            IconButton(
                onClick = { expanded.value = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu")
            }
        },
        actions = {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(
                    text = { Text("About Us") },
                    onClick = {
                        expanded.value = false
                        onMenuItemClick("AboutUs")
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTopBarPreview() {
    CustomTopBar(
        title = "Jual Beli Beras Ponorogo",
        onMenuItemClick = {}
    )
}