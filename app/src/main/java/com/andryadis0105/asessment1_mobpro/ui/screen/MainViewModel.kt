package com.andryadis0105.asessment1_mobpro.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andryadis0105.asessment1_mobpro.model.Hewan
import com.andryadis0105.asessment1_mobpro.network.ApiStatus
import com.andryadis0105.asessment1_mobpro.network.HewanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.net.UnknownHostException
import java.net.SocketTimeoutException
import java.io.IOException

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Hewan>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val combinedData = mutableListOf<Hewan>()

                // 1. Ambil data dari Dog API (data publik)
                val dogApiData = HewanApi.getHewan(userId)
                combinedData.addAll(dogApiData)

                // 2. Ambil data dari API lama (data user)
                try {
                    val userApiData = HewanApi.getUserHewan(userId)
                    combinedData.addAll(userApiData)
                } catch (e: Exception) {
                    Log.d("MainViewModel", "Failed to load user data: ${e.message}")
                    // Tidak throw error, karena Dog API data masih berhasil
                }

                // Jika tidak ada data sama sekali dan terjadi error koneksi
                if (combinedData.isEmpty()) {
                    status.value = ApiStatus.FAILED
                } else {
                    data.value = combinedData
                    status.value = ApiStatus.SUCCESS
                }

            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")

                // Handle different types of network errors
                when (e) {
                    is UnknownHostException,
                    is SocketTimeoutException,
                    is IOException -> {
                        // Network connection error
                        status.value = ApiStatus.FAILED
                    }
                    else -> {
                        // Other errors
                        status.value = ApiStatus.FAILED
                        errorMessage.value = "Error: ${e.message}"
                    }
                }
            }
        }
    }

    fun saveData(userId: String, nama: String, namaLatin: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Menggunakan oldService untuk insert (API lama)
                val result = HewanApi.oldService.postHewan(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    namaLatin.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success") {
                    retrieveData(userId) // Refresh untuk menampilkan data baru
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")

                // Handle network errors gracefully
                val errorMsg = when (e) {
                    is UnknownHostException -> "Tidak ada koneksi internet"
                    is SocketTimeoutException -> "Koneksi timeout"
                    is IOException -> "Gagal terhubung ke server"
                    else -> "Error: ${e.message}"
                }
                errorMessage.value = errorMsg
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun deleteData(userId: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Menggunakan oldService untuk delete (API lama)
                val result = HewanApi.oldService.deleteHewan(userId, id)
                if (result.status == "success") {
                    retrieveData(userId) // Refresh data setelah delete
                } else {
                    errorMessage.value = result.message ?: "Gagal menghapus"
                }
            } catch (e: Exception) {
                val errorMsg = when (e) {
                    is UnknownHostException -> "Tidak ada koneksi internet"
                    is SocketTimeoutException -> "Koneksi timeout"
                    is IOException -> "Gagal terhubung ke server"
                    else -> "Error: ${e.message}"
                }
                errorMessage.value = errorMsg
            }
        }
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}