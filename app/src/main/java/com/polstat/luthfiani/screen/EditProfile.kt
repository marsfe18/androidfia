package com.polstat.luthfiani.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.luthfiani.components.Email
import com.polstat.luthfiani.components.NormalTextField
import com.polstat.luthfiani.model.UserProfile
import com.polstat.luthfiani.state.EmailState
import com.polstat.luthfiani.state.NormalState
import com.polstat.luthfiani.viewModel.UserViewModel
import com.polstat.luthfiani.viewModel.UserViewModelFactory

@Composable
fun EditProfileRoute(userViewModel: UserViewModel,
    setelahEdit:() -> Unit) {
    val context = LocalContext.current
    val userProfile = userViewModel.user.observeAsState()
    val errorMessage = userViewModel.errorMessage.value
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { userViewModel.clearMessageError() },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { userViewModel.clearMessageError() }) {
                    Text("OK")
                }
            }
        )
    }
    FormAuthScreen {
        EditProfile(setelahEdit = {email, firstname, lastname ->
            userViewModel.editAkun(email, firstname, lastname, context) {
                setelahEdit()
            }
        }, userProfile.value)
    }
}

@Composable
fun EditProfile(
    setelahEdit: (email: String, firstname: String, lastname: String) -> Unit,
    value: UserProfile?,
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        val emailState = remember { EmailState(value?.email) }
        val namaState = remember { NormalState(value?.namaDepan) }
        val namaBelakang = remember { NormalState(value?.namaBelakang) }
        NormalTextField(label = "Nama depan", textState = namaState)
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextField(label = "Nama belakang", textState = namaBelakang)
        Spacer(modifier = Modifier.height(16.dp))
        Email(enable = false ,emailState)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { setelahEdit(emailState.text, namaState.text, namaBelakang.text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid && namaState.isValid && namaBelakang.isValid
        ) {
            Text(text = "Simpan perubahan")
        }
    }
}