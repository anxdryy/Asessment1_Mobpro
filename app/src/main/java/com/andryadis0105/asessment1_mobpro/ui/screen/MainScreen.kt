package com.andryadis0105.asessment1_mobpro.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andryadis0105.asessment1_mobpro.R
import com.andryadis0105.asessment1_mobpro.model.Catatan
import com.andryadis0105.asessment1_mobpro.navigation.Screen
import com.andryadis0105.asessment1_mobpro.ui.theme.Asessment1_MobproTheme
import com.andryadis0105.asessment1_mobpro.util.SettingsDataStore
import com.andryadis0105.asessment1_mobpro.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ListItem(catatan: Catatan, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(text = catatan.nama) },
        supportingContent = { Text(text = catatan.nim) },
        trailingContent = { Text(text = catatan.kelas) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun GridItem(catatan: Catatan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = catatan.nama, style = MaterialTheme.typography.titleMedium)
            Text(text = catatan.nim, style = MaterialTheme.typography.bodyMedium)
            Text(text = catatan.kelas, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = SettingsDataStore(context)
    val showList by dataStore.layoutFlow.collectAsState(true)
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)

    var searchQuery by remember { mutableStateOf("") }

    val activeNotes by viewModel.activeNotes.collectAsState()
    val filteredData = if (searchQuery.isBlank()) {
        activeNotes
    } else {
        activeNotes.filter {
            it.nama.contains(searchQuery, ignoreCase = true) ||
                    it.nim.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(onClick = {
                            val currentSort = viewModel.sortType.value
                            val newSort = when (currentSort) {
                                SortType.BY_KELAS -> SortType.BY_NAMA
                                SortType.BY_NAMA -> SortType.BY_NIM
                                SortType.BY_NIM -> SortType.BY_KELAS
                            }
                            viewModel.setSortType(newSort)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_sort_24),
                                contentDescription = "Sort",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.RecycleBin.route)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_delete_24),
                                contentDescription = "Recycle Bin",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStore.saveLayout(!showList)
                            }
                        }) {
                            Icon(
                                painter = painterResource(
                                    if (showList) R.drawable.baseline_grid_view_24
                                    else R.drawable.baseline_view_list_24
                                ),
                                contentDescription = stringResource(
                                    if (showList) R.string.grid
                                    else R.string.list
                                ),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* Handle search */ },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.FormBaru.route) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        if (filteredData.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.list_kosong))
            }
        } else {
            if (showList) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 84.dp)
                ) {
                    items(filteredData) { item ->
                        ListItem(catatan = item) {
                            navController.navigate(Screen.FormUbah.withId(item.id))
                        }
                        Divider()
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
                ) {
                    items(filteredData) { item ->
                        GridItem(catatan = item) {
                            navController.navigate(Screen.FormUbah.withId(item.id))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asessment1_MobproTheme {
        MainScreen(rememberNavController())
    }
}