package com.andryadis0105.asessment1_mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andryadis0105.asessment1_mobpro.database.CatatanDao
import com.andryadis0105.asessment1_mobpro.model.Catatan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val dao: CatatanDao) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.BY_KELAS)
    val sortType: StateFlow<SortType> = _sortType


    @OptIn(ExperimentalCoroutinesApi::class)
    val activeNotes: StateFlow<List<Catatan>> = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.BY_NAMA -> dao.getCatatanSortByNama()
                SortType.BY_NIM -> dao.getCatatanSortByNim()
                else -> dao.getActiveCatatan()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val deletedNotes = dao.getDeletedCatatan()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setSortType(type: SortType) {
        _sortType.value = type
    }

    fun restoreNote(id: Long) {
        viewModelScope.launch {
            dao.restoreById(id)
        }
    }

    fun permanentDelete(id: Long) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

    fun softDelete(id: Long) {
        viewModelScope.launch {
            dao.softDeleteById(id, System.currentTimeMillis())
        }
    }
}

enum class SortType {
    BY_NAMA, BY_NIM, BY_KELAS
}