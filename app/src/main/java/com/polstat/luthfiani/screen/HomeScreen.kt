package com.polstat.luthfiani.screen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.polstat.luthfiani.components.HomeTopBar
import com.polstat.luthfiani.model.ErrorResponse
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.screen.utasan.TitleWithVerifiedStatus
import com.polstat.luthfiani.viewModel.UtasViewModel
import com.polstat.luthfiani.viewModel.UtasViewModelFactory

@Composable
fun HomeScreen(
    pageNumber: Int,
    navController: NavController,
    gantiPage: (Int) -> Unit,
    setelahKlikDetail:(Int)-> Unit,
    createUtasan: () -> Unit,
    editUtasan: (Utas) -> Unit,
) {
    val context: Context = LocalContext.current
    val utasViewModel: UtasViewModel = viewModel(factory = UtasViewModelFactory() )
    LaunchedEffect(key1 = Unit) {
        utasViewModel.loadAllUtasan(context)
        utasViewModel.loadMyUtasan(context)
    }

    val allUtasan by utasViewModel.utasan.observeAsState(listOf())
    val myUtasan by utasViewModel.myUtasan.observeAsState(listOf())
    var showDialog by remember {
        mutableStateOf(false)
    }
    var utasIdToDelete by remember { mutableStateOf<Int?>(null) }

    // Dialog konfirmasi penghapusan
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi Penghapusan") },
            text = { Text("Apakah Anda yakin ingin menghapus utasan ini?") },
            confirmButton = {
                Button(onClick = {
                    utasIdToDelete?.let { id ->
                        utasViewModel.delete(context, id, gagal = {  })
                    }
                    showDialog = false
                }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    ErrorDialog(utasViewModel = utasViewModel)

    Scaffold(topBar = {
        HomeTopBar(pageNumber = pageNumber, gantiPage = gantiPage)
    }, floatingActionButton = {
        FloatingActionButton(onClick = { createUtasan() }) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "tambah utasan")
        }
    })
    {paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (pageNumber) {
                0 -> UtasanList(allUtasan,pageNumber , setelahKlikDetail, editKliked = {}, deleteKliked = {})
                1 -> UtasanList(
                    myUtasan,
                    pageNumber,
                    setelahKlikDetail,
                    editKliked = { editUtasan(it) },
                    deleteKliked = {
                        utasIdToDelete = it
                        showDialog = true
                    })
            }
        }
    }
}


@Composable
fun UtasanList(
    utasans: List<Utas>, pageNumber: Int, setelahKlikDetail: (Int) -> Unit,
    editKliked: (Utas) -> Unit, deleteKliked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        this.items(utasans) {utasan ->
            UtasanItem(utasan = utasan, pageNumber, onClick = setelahKlikDetail, editKliked, deleteKliked)
        }
    }
}

@Composable
fun UtasanItem(
    utasan: Utas, myPage: Int,
    onClick: (Int) -> Unit,
    editKliked: (Utas) -> Unit,
    deleteKliked: (Int) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(utasan.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Oleh: ${utasan.user}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = utasan.waktu,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TitleWithVerifiedStatus(utasan.judul, utasan.role)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = utasan.isi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (isExpanded && myPage == 1) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { editKliked(utasan) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { deleteKliked(utasan.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
            Button(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)) {
                Text(text = if (isExpanded) "Lebih sedikit" else "lebih lengkap")
            }
        }

    }
}

@Composable
fun ErrorDialog(utasViewModel: UtasViewModel) {
    val message by utasViewModel.message.observeAsState()

    if (message != null) {
        AlertDialog(
            onDismissRequest = {
                utasViewModel.clearMessage()
            },
            title = { Text("Error") },
            text = { Text(message ?: "") },
            confirmButton = {
                Button(onClick = {
                    utasViewModel.clearMessage()
                }) {
                    Text("OK")
                }
            }
        )
    }
}


@Preview
@Composable
fun PrevHome() {
    HomeScreen(pageNumber = 0, navController = rememberNavController(), gantiPage = {}, setelahKlikDetail = {}, createUtasan = {} , editUtasan = {})
}
