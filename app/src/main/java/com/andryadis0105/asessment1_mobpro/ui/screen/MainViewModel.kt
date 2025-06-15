package com.andryadis0105.asessment1_mobpro.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andryadis0105.asessment1_mobpro.model.Kamus
import com.andryadis0105.asessment1_mobpro.network.KamusApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream


class MainViewModel : ViewModel() {
    private val _status = MutableStateFlow(ApiStatus.LOADING)
    val status = _status

    private val _data = mutableStateOf(emptyList<Kamus>())
    val data: State<List<Kamus>> = _data

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun retrieveData(authorization: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = ApiStatus.LOADING
            try {
                _data.value = KamusApi.service.getKamus(authorization)
                _status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error retrieving data: ${e.message}")
                _errorMessage.value = "Failed to load data: ${e.message}"
                _status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(
        authorization: String,
        bahasaIndonesia: String,
        bahasaInggris: String,
        bitmap: Bitmap?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (bitmap != null) {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val byteArray = stream.toByteArray()
                    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    val multipartBody = MultipartBody.Part.createFormData("gambar", "image.jpg", requestBody)

                    KamusApi.service.postKamus(
                        authorization,
                        bahasaIndonesia.toRequestBody("text/plain".toMediaTypeOrNull()),
                        bahasaInggris.toRequestBody("text/plain".toMediaTypeOrNull()),
                        multipartBody
                    )
                } else {
                    KamusApi.service.postKamusWithoutImage(
                        authorization,
                        bahasaIndonesia,
                        bahasaInggris
                    )
                }
                retrieveData(authorization)
                _errorMessage.value = "Data saved successfully!"
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error saving data: ${e.message}")
                _errorMessage.value = "Failed to save data: ${e.message}"
            }
        }
    }

    fun updateData(
        authorization: String,
        id: String,
        bahasaIndonesia: String,
        bahasaInggris: String,
        bitmap: Bitmap?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (bitmap != null) {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val byteArray = stream.toByteArray()
                    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    val multipartBody = MultipartBody.Part.createFormData("gambar", "image.jpg", requestBody)

                    KamusApi.service.updateKamus(
                        authorization,
                        id,
                        bahasaIndonesia.toRequestBody("text/plain".toMediaTypeOrNull()),
                        bahasaInggris.toRequestBody("text/plain".toMediaTypeOrNull()),
                        multipartBody
                    )
                } else {
                    KamusApi.service.updateKamusWithoutImage(
                        authorization,
                        id,
                        bahasaIndonesia,
                        bahasaInggris
                    )
                }
                retrieveData(authorization)
                _errorMessage.value = "Data updated successfully!"
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error updating data: ${e.message}")
                _errorMessage.value = "Failed to update data: ${e.message}"
            }
        }
    }

    fun deleteData(authorization: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                KamusApi.service.deleteKamus(authorization, id)
                retrieveData(authorization)
                _errorMessage.value = "Data deleted successfully!"
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error deleting data: ${e.message}")
                _errorMessage.value = "Failed to delete data: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _errorMessage.value = null
    }
}