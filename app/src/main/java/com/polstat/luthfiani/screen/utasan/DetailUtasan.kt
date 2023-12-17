package com.polstat.luthfiani.screen.utasan

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.luthfiani.viewModel.UserViewModel
import com.polstat.luthfiani.viewModel.UserViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Recycling
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.polstat.luthfiani.components.CustomInputField
import com.polstat.luthfiani.model.KomenDto
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.util.getFileNameFromUri
import com.polstat.luthfiani.util.getMimeType
import com.polstat.luthfiani.viewModel.SingleUtasViewModel
import com.polstat.luthfiani.viewModel.SingleUtasViewModelFactory


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRoute(
    id: String, onback: () -> Unit ,
    singleUtasViewModel: SingleUtasViewModel = viewModel(factory = SingleUtasViewModelFactory()),
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory()),
    gagalGetDetail: () -> Unit
) {
    val context = LocalContext.current
    var showButtonReload by remember { mutableStateOf(false) }
    val ketikKomens = rememberSaveable { mutableStateOf("") }
    val utas = singleUtasViewModel.utasan.observeAsState()
    val komens = singleUtasViewModel.comment.observeAsState()
    val file = remember { mutableStateOf(Uri.EMPTY) }
    val mimeType = remember { mutableStateOf("") }
    val fileState = remember { mutableStateOf("")}
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            file.value = uri
            fileState.value = getFileNameFromUri(context, uri)
            getMimeType(context, uri)?.let {
                mimeType.value = it
            }
        }
    )

    BackHandler {
        onback()
    }

    LaunchedEffect(key1 = Unit) {
        singleUtasViewModel.getUtasById(id, context, gagal = { gagalGetDetail() })
        singleUtasViewModel.getComent(id, context, gagal = { showButtonReload = true })
    }

    Column {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = {
                        focusManager.clearFocus()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            utas.value?.let {
                DiscussionScreen(
                    downloadFile = {id ->
                        singleUtasViewModel.downloadFile(context, id)
                    },
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    it
                ) {
                    if (showButtonReload) {
                        IconButton(onClick = { singleUtasViewModel.getComent(id, context, gagal = { showButtonReload = true }) }) {
                            Icon(imageVector = Icons.Rounded.Recycling, contentDescription = "Reload")
                        }
                    }
                    if (komens.value != null) {
                        ListComent(komens = komens.value!!, downloadFile = {docId ->
                            singleUtasViewModel.downloadFile(context, docId.toInt())
                        })
                    }
                }
            }

            HorizontalDivider(Modifier.fillMaxWidth())
//            Spacer(modifier = Modifier.height(8.dp))
            Column(Modifier.padding(10.dp)) {
                if (fileState.value.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "File: ${fileState.value}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                        IconButton(
                            onClick = {
                                // Clear the file state and reset the variables
                                file.value = Uri.EMPTY
                                fileState.value = ""
                                mimeType.value = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove File",
                                tint = Color.Gray
                            )
                        }
                    }
                }
                CustomInputField(textValue = ketikKomens.value,
                    onTextChange = { ketikKomens.value = it },
                    onAttachClick = { pickFileLauncher.launch("*/*") },
                    onSendClick = {
                        singleUtasViewModel.addComment(id, ketikKomens.value, file.value, context, gagal = { onback() })
                        focusManager.clearFocus()
                        ketikKomens.value = ""
                    }
                )
            }
        }
    }

}



@Composable
fun DiscussionScreen(downloadFile: (Int) -> Unit, modifier: Modifier, utas: Utas, comment: @Composable () -> Unit) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TitleWithVerifiedStatus(utas.judul, utas.role)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    utas.isi,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${utas.user} * ${utas.waktu}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(24.dp))
                    Row(Modifier.clickable(onClick = { downloadFile(utas.doc) })) {
                        Icon(imageVector = Icons.Default.FileDownload, contentDescription = "Download File", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("File", color = Color.Gray)
                    }
                }
            }
        }
        Row {
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(30.dp))
            comment()
        }
    }
}

@Composable
fun TitleWithVerifiedStatus(title: String, role: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        if (role == "STAFF" || role == "ADMIN") {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified",
                tint = if (role == "ADMIN") Color(0xFF4CAF50) else Color(0xFF2196F3)
            )
        }
    }
}


@Composable
fun ListComent(komens: List<KomenDto>, downloadFile: (String) -> Unit) {
    Column {
        komens.forEach {
            CommentItem(it, downloadFile)
        }
    }


}

@Composable
fun CommentItem(komenDto: KomenDto, downloadFile: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UserDisplayName(user = komenDto.user, role = komenDto.role)
                Text(
                    text = komenDto.waktu, // Format waktu sesuai kebutuhan
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (komenDto.isi.isNotEmpty()) {
                Text(komenDto.isi, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
            }

            komenDto.doc?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { downloadFile(komenDto.doc) }
                ) {
                    Icon(imageVector = Icons.Default.FileDownload, contentDescription = "file", tint = Color.Gray)
                    Text(text = "File", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun UserDisplayName(user: String, role: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = user,
            fontWeight = FontWeight.Bold,
            color = when (role) {
                "ADMIN" -> Color(0xFF4CAF50) // Hijau untuk admin
                "STAFF" -> Color(0xFF2196F3) // Biru untuk staff
                else -> MaterialTheme.colorScheme.onSurface // Warna default
            }
        )
        if (role in listOf("ADMIN", "STAFF")) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified",
                tint = when (role) {
                    "ADMIN" -> Color(0xFF4CAF50)
                    "STAFF" -> Color(0xFF2196F3)
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

