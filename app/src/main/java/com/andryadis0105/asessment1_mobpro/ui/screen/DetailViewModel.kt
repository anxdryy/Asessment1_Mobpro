package com.andryadis0105.asessment1_mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andryadis0105.asessment1_mobpro.database.CatatanDao
import com.andryadis0105.asessment1_mobpro.model.Catatan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: CatatanDao) : ViewModel() {
    fun insert(nama: String, nim: String, kelas: String) {
        val catatan = Catatan(
            nama = nama,
            nim = nim,
            kelas = kelas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(catatan)
        }
    }

    suspend fun getCatatan(id: Long): Catatan? {
        return dao.getCatatanById(id)
    }

    fun update(id: Long, nama: String, nim: String, kelas: String) {
        val catatan = Catatan(
            id = id,
            nama = nama,
            nim = nim,
            kelas = kelas
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(catatan)
        }
    }


    fun softDelete(id: Long) {
        viewModelScope.launch {
            dao.softDeleteById(id, System.currentTimeMillis())
        }
    }
}