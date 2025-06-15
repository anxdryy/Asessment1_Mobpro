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
import com.andryadis0105.asessment1_mobpro.model.Kamus
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.andryadis0105.asessment1_mobpro.R
import com.andryadis0105.asessment1_mobpro.network.KamusApi

@Composable
fun KamusDialog(
    bitmap: Bitmap?,
    kamus: Kamus?,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit
){

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

                Text(text = if (kamus == null) "Tambah Kata Baru" else "Edit Kata")

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(top = 8.dp)
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
                            .padding(top = 8.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        placeholder = painterResource(R.drawable.loading_img),
                        error = painterResource(R.drawable.baseline_broken_image_24)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "No Image Selected",
                        modifier = Modifier
                            .size(128.dp)
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

        KamusDialog(
            bitmap = null,
            kamus = null,
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

        KamusDialog(
            bitmap = null,
            kamus = Kamus(
                id = "123",
                bahasaIndonesia = "Rumah",
                bahasaInggris = "House",
                gambar = "dummy_image_path.jpg",
                Authorization = "user@example.com"
            ),
            onDismissRequest = { },
            onConfirmation = { _, _ -> }
        )
    }
}