package com.andryadis0105.asessment1_mobpro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andryadis0105.asessment1_mobpro.R
import com.andryadis0105.asessment1_mobpro.model.Catatan
import com.andryadis0105.asessment1_mobpro.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(navController: NavHostController) {
    val factory = ViewModelFactory(LocalContext.current)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val deletedNotes by viewModel.deletedNotes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Catatan?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { Text(stringResource(R.string.recycle_bin)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        if (deletedNotes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.recycle_bin_empty))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(deletedNotes) { note ->
                    DeletedNoteItem(
                        note = note,
                        onRestore = { viewModel.restoreNote(note.id) },
                        onDelete = {
                            noteToDelete = note
                            showDialog = true
                        }
                    )
                    Divider()
                }
            }
        }
    }

    if (showDialog && noteToDelete != null) {
        DisplaAlertDialog(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                noteToDelete?.let { viewModel.permanentDelete(it.id) }
                showDialog = false
            }
        )
    }
}

@Composable
fun DeletedNoteItem(
    note: Catatan,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val deletedDate = note.deletionDate?.let { dateFormat.format(Date(it)) } ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = note.nama, fontWeight = FontWeight.Bold)
            Text(text = note.nim)
            Text(text = note.kelas)
            if (deletedDate.isNotEmpty()) {
                Text(
                    text = "Deleted: $deletedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onRestore) {
                    Text(stringResource(R.string.restore))
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.hapus_permanen))
                }
            }
        }
    }
}
