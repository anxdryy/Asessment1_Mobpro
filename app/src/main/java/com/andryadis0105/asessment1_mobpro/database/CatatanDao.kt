package com.andryadis0105.asessment1_mobpro.database

import androidx.room.*
import com.andryadis0105.asessment1_mobpro.model.Catatan
import kotlinx.coroutines.flow.Flow

@Dao
interface CatatanDao {

    @Insert
    suspend fun insert(catatan: Catatan)

    @Update
    suspend fun update(catatan: Catatan)

    @Query("SELECT * FROM mahasiswa WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?

    @Query("DELETE FROM mahasiswa WHERE id = :id")
    suspend fun deleteById(id: Long)


    @Query("SELECT * FROM mahasiswa WHERE isDeleted = 0 ORDER BY nama ASC")
    fun getCatatanSortByNama(): Flow<List<Catatan>>

    @Query("SELECT * FROM mahasiswa WHERE isDeleted = 0 ORDER BY nim ASC")
    fun getCatatanSortByNim(): Flow<List<Catatan>>

    @Query("SELECT * FROM mahasiswa WHERE isDeleted = 0 ORDER BY kelas ASC")
    fun getActiveCatatan(): Flow<List<Catatan>>


    @Query("SELECT * FROM mahasiswa WHERE isDeleted = 1 ORDER BY deletionDate DESC")
    fun getDeletedCatatan(): Flow<List<Catatan>>


    @Query("UPDATE mahasiswa SET isDeleted = 1, deletionDate = :timestamp WHERE id = :id")
    suspend fun softDeleteById(id: Long, timestamp: Long)


    @Query("UPDATE mahasiswa SET isDeleted = 0, deletionDate = NULL WHERE id = :id")
    suspend fun restoreById(id: Long)
}