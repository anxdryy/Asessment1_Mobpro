package com.andryadis0105.asessment1_mobpro.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.andryadis0105.asessment1_mobpro.ui.theme.Asessment1_MobproTheme
import com.andryadis0105.asessment1_mobpro.model.Kamus // Import your Kamus data class
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.andryadis0105.asessment1_mobpro.R // Assuming you have placeholder/error drawables here
import com.andryadis0105.asessment1_mobpro.network.KamusApi // Import KamusApi for image URL

@Composable
fun KamusDialog(
    bitmap: Bitmap?,
    kamus: Kamus?, // <-- ADD THIS PARAMETER for editing
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit
){
    // Initialize state with values from 'kamus' if it's in edit mode, otherwise empty
    var bahasaIndonesia by remember { mutableStateOf(kamus?.bahasaIndonesia ?: "") }
    var bahasaInggris by remember { mutableStateOf(kamus?.bahasaInggris ?: "") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Dynamic title for the dialog
                Text(text = if (kamus == null) "Tambah Kata Baru" else "Edit Kata")

                // Image display logic:
                // 1. Show newly selected bitmap if available
                // 2. Otherwise, show existing kamus image if available
                // 3. Otherwise, show a generic placeholder image
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(top = 8.dp) // Add some padding
                    )
                } else if (kamus?.gambar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(KamusApi.getKamusImageUrl(kamus.gambar))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Existing Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(top = 8.dp), // Add some padding
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop, // Use full path to avoid ambiguity
                        placeholder = painterResource(R.drawable.loading_img), // Make sure you have these drawables
                        error = painterResource(R.drawable.baseline_broken_image_24) // Make sure you have these drawables
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image_24), // Generic image placeholder
                        contentDescription = "No Image Selected",
                        modifier = Modifier
                            .size(128.dp) // Set a fixed size for the placeholder
                            .padding(top = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = bahasaIndonesia,
                    onValueChange = { bahasaIndonesia = it },
                    label = { Text(text = "Bahasa Indonesia") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = bahasaInggris,
                    onValueChange = { bahasaInggris = it },
                    label = { Text(text = "Bahasa Inggris") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row (
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    OutlinedButton (
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ){
                        Text(text = "Batal")
                    }
                    OutlinedButton(
                        onClick = { onConfirmation(bahasaIndonesia, bahasaInggris) },
                        enabled = bahasaIndonesia.isNotEmpty() && bahasaInggris.isNotEmpty(),
                        modifier = Modifier.padding(8.dp)
                    ){
                        // Dynamic button text
                        Text(text = if (kamus == null) "Simpan" else "Update")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun KamusDialogPreview(){
    Asessment1_MobproTheme {
        // Preview for adding a new Kamus entry
        KamusDialog(
            bitmap = null,
            kamus = null, // No existing Kamus for "add" preview
            onDismissRequest = { },
            onConfirmation = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun KamusDialogEditPreview(){
    Asessment1_MobproTheme {
        // Preview for editing an existing Kamus entry
        KamusDialog(
            bitmap = null,
            kamus = Kamus(
                id = "123",
                bahasaIndonesia = "Rumah",
                bahasaInggris = "House",
                gambar = "dummy_image_path.jpg", // Use a dummy path for preview
                Authorization = "user@example.com"
            ),
            onDismissRequest = { },
            onConfirmation = { _, _ -> }
        )
    }
}