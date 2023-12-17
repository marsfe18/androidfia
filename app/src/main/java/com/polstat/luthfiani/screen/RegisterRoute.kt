package com.polstat.luthfiani.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.luthfiani.viewModel.RegisterModelFactory
import com.polstat.luthfiani.viewModel.RegisterViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polstat.luthfiani.components.AuthBottomBar
import com.polstat.luthfiani.components.Email
import com.polstat.luthfiani.components.NormalTextField
import com.polstat.luthfiani.components.Password
import com.polstat.luthfiani.components.WelcomeTopBar
import com.polstat.luthfiani.components.stronglyDeemphasizedAlpha
import com.polstat.luthfiani.state.ConfirmPasswordState
import com.polstat.luthfiani.state.EmailState
import com.polstat.luthfiani.state.NormalState
import com.polstat.luthfiani.state.PasswordState
import com.polstat.luthfiani.ui.theme.LuthfianiTheme


@Composable
fun SignUpRoute(
    berhasilRegister: (String) -> Unit,
    pindahKeLogin: () -> Unit,
    onNavUp: () -> Unit,
) {
    val signInViewModel: RegisterViewModel = viewModel(factory = RegisterModelFactory())
    val context = LocalContext.current
    SignUpScreen(
        onSignUpSubmitted = { email, password, firstname, lastname ->
            signInViewModel.register(email, password,firstname, lastname, context = context ,
                onSignInComplete = berhasilRegister)
        },
        onNavUp = onNavUp,
        pindahKeLogin = pindahKeLogin
    )
}


@Composable
fun SignUpScreen(
    onSignUpSubmitted: (email: String, password: String, firstname: String, lastname: String) -> Unit,
    onNavUp: () -> Unit,
    pindahKeLogin: () -> Unit,
) {
    Scaffold(
        topBar = {
            WelcomeTopBar(topAppBarText = "Register") {
                onNavUp()
            }
        },
        content = { contentPadding ->
            FormAuthScreen (
                modifier = Modifier.padding(contentPadding)
            ) {
                SignUpContent(onSignUpSubmitted = onSignUpSubmitted)
            }           
        },
        bottomBar = {
            AuthBottomBar(
                textButton = "Login",
                textNormal = "Sudah punya akun? "
            ) {
                pindahKeLogin()
            }
        }
    )
}

@Composable
fun SignUpContent(
    onSignUpSubmitted: (email: String, password: String, firstname: String, lastname: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val emailState = remember { EmailState("") }
        val namaState = remember { NormalState("") }
        val namaBelakang = remember { NormalState("") }
        NormalTextField(label = "Nama depan", textState = namaState)
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextField(label = "Nama belakang", textState = namaBelakang)
        Spacer(modifier = Modifier.height(16.dp))
        Email(enable = true,emailState, onImeAction = { passwordFocusRequest.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        Password(
            label = "Password",
            passwordState = passwordState,
            imeAction = ImeAction.Next,
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))
        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }
        Password(
            label = "Confirm Password",
            passwordState = confirmPasswordState,
            onImeAction = { onSignUpSubmitted(emailState.text, passwordState.text, namaState.text, namaBelakang.text) },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Dengan menekan tombol register maka anda sudah menyetujui term and condifiton",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = stronglyDeemphasizedAlpha)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignUpSubmitted(emailState.text, passwordState.text, namaState.text, namaBelakang.text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid &&
                    passwordState.isValid && confirmPasswordState.isValid
                    && namaState.isValid && namaBelakang.isValid
        ) {
            Text(text = "Buat akun baru")
        }
    }
}

@Preview()
@Composable
fun SignUpPreview() {
    LuthfianiTheme {
        SignUpScreen(
            onSignUpSubmitted = { email, password, firstname, last ->  },
            {

            }
        ) {}
    }
}




