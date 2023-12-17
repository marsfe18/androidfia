package com.polstat.luthfiani.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.luthfiani.components.Password
import com.polstat.luthfiani.state.ConfirmPasswordState
import com.polstat.luthfiani.state.PasswordState
import com.polstat.luthfiani.viewModel.UserViewModel
import com.polstat.luthfiani.viewModel.UserViewModelFactory

@Composable
fun EditPwRoute(
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory() ),
    berhasilGantiPw: () -> Unit
) {
    val errorMessage = userViewModel.errorMessage.value
    val context = LocalContext.current
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
        EditPwScreen(setelahKlikSubmit = { newPassword, oldPassword ->
            userViewModel.editPassword(context, newPassword, oldPassword) {
                berhasilGantiPw()
            }
        })
    }
}

@Composable
fun EditPwScreen(setelahKlikSubmit: (newPassword:String, oldPassword: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        val oldPassword = remember { PasswordState() }
        Password(
            label = "Password baru",
            passwordState = oldPassword,
            imeAction = ImeAction.Next
        )
        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        Password(
            label = "Password baru",
            passwordState = passwordState,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))
        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }
        Password(
            label = "Confirm Password",
            passwordState = confirmPasswordState,
            onImeAction = {setelahKlikSubmit(passwordState.text, oldPassword.text) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { setelahKlikSubmit(passwordState.text, oldPassword.text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = passwordState.isValid && confirmPasswordState.isValid
        ) {
            Text(text = "Ganti password")
        }
    }
}