package com.andryadis0105.asessment1_mobpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mahasiswa")
data class Catatan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val nim: String,
    val kelas: String,
    val isDeleted: Boolean = false,
    val deletionDate: Long? = null
)