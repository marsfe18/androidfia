package com.polstat.luthfiani.screen.utasan

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.luthfiani.components.NormalTextField
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.screen.FormAuthScreen
import com.polstat.luthfiani.state.NormalState
import com.polstat.luthfiani.util.getFileNameFromUri
import com.polstat.luthfiani.util.getMimeType
import com.polstat.luthfiani.viewModel.UtasViewModel
import com.polstat.luthfiani.viewModel.UtasViewModelFactory

@Composable
fun FormUtasanScreen(
    utasan: Utas?,
    utasViewModel: UtasViewModel = viewModel(factory = UtasViewModelFactory()),
    successTambah: (id: Int) -> Unit,
    onBack: () -> Unit
) {
//    val utasan = utasViewModel.toEditUtas.value
    val errorMessage = utasViewModel.message.observeAsState()
    val context = LocalContext.current
    BackHandler {
        onBack()
    }
    errorMessage.value?.let {
        AlertDialog(
            onDismissRequest = { utasViewModel.clearMessage() },
            title = { Text("Error") },
            text = {  Text(it)  },
            confirmButton = {
                Button(onClick = { utasViewModel.clearMessage() }) {
                    Text("OK")
                }
            }
        )
    }

    Column {
        FormUtasan(context,utasan, afterTambah = { judul, topik, isi, file, mimeType ->
            if (utasan == null) {
                utasViewModel.addUtasan(
                    judul, topik, isi, file, mimeType,
                    context, successTambah
                )
            } else {
                utasViewModel.editUtas( utasan.id,
                    judul, topik, isi,
                    context, successTambah
                )
            }
        })
    }
}

@Composable
fun FormUtasan(
    context: Context,
    utasan: Utas?,
    afterTambah: (judul: String, topik: String, isi: String, file: Uri?, mimeType: String) -> Unit
) {
    FormAuthScreen {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            var text = if (utasan== null) "" else utasan.topik.toString()
            val judul = remember { NormalState(utasan?.judul ?: "") }
            var topik = remember { NormalState( text ) }
            val isi = remember { NormalState(utasan?.isi ?: "") }
            val file = remember { mutableStateOf(Uri.EMPTY) }
            val mimeType = remember { mutableStateOf("") }
            val fileState = remember { mutableStateOf("Pilih file") }
            val topikOptions = listOf("Keluhan", "Laporan", "Informasi")
            var expanded by remember { mutableStateOf(false) }

            val pickFileLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri: Uri? ->
                    uri?.let {
                        file.value = it
                        fileState.value = getFileNameFromUri(context, uri)
                        mimeType.value = getMimeType(context, uri) ?: ""
                    }
                }
            )

            OutlinedTextField(
                value = judul.text,
                onValueChange = { judul.text = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(16.dp)) {
                    Text(
                        text = if (topik.text == "") "Pilih Topik" else topik.text,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )


                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    topikOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            when(option) {
                                "Keluhan" -> topik.text = "1"
                                "Informasi" -> topik.text = "2"
                                "Laporan" -> topik.text = "3"
                            }
                            expanded = false
                        }, text = {
                            Text(option)
                        })
                    }
                }
            }



            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = isi.text,
                onValueChange = { isi.text = it },
                label = { Text("Isi") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilePickerButton(
                fileState = fileState,
                pickFileLauncher = pickFileLauncher,
                enabled = utasan == null
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubmitButton(
                enabled = judul.isValid && topik.isValid && isi.isValid,
                onSubmit = { afterTambah(judul.text, topik.text, isi.text, file.value, mimeType.value) }
            )
        }
    }
}

@Composable
fun FilePickerButton(fileState: MutableState<String>, pickFileLauncher: ActivityResultLauncher<String>, enabled: Boolean) {
    Button(
        onClick = { pickFileLauncher.launch("*/*") },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f)
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = fileState.value, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.Folder, contentDescription = "Pilih file")
    }
}

@Composable
fun SubmitButton(enabled: Boolean, onSubmit: () -> Unit) {
    Button(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(text = "Buat Utasan")
    }
}




//@Preview
//@Composable
//fun FormPrev() {
//    FormUtasan(LocalContext.current.applicationContext,utasan = null) {
//
//    }
//}
