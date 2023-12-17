package com.polstat.luthfiani.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.luthfiani.components.CustomButton
import com.polstat.luthfiani.components.Email
import com.polstat.luthfiani.components.Password
import com.polstat.luthfiani.components.AuthBottomBar
import com.polstat.luthfiani.components.WelcomeBackText
import com.polstat.luthfiani.components.WelcomeTopBar
import com.polstat.luthfiani.state.EmailState
import com.polstat.luthfiani.state.EmailStateSaver
import com.polstat.luthfiani.state.PasswordState
import com.polstat.luthfiani.util.supportWideScreen
import com.polstat.luthfiani.viewModel.LoginViewModel
import com.polstat.luthfiani.viewModel.SignInViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    initialEmail: String?,
    berhasilLogin: () -> Unit,
    setelahKlikRegister: () -> Unit,
    onNavUp: () -> Unit,
    setelahKlikLupaPw: () -> Unit
) {
    val localContext = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(factory = SignInViewModelFactory())
    MainLoginScreen(
        email = initialEmail,
        setelahKlikLogin = { email, password ->
            loginViewModel.signIn(email, password, localContext) {
                berhasilLogin()
            }
        },
        onNavUp = onNavUp,
        setelahKlikRegister = setelahKlikRegister,
        setelahKlikLupaPw = setelahKlikLupaPw,
        loginViewModel = loginViewModel
    )
}




@Composable
fun MainLoginScreen(
    email: String? = "",
    setelahKlikLogin: (email:String, password:String) -> Unit,
    onNavUp: () -> Unit,
    setelahKlikRegister: () -> Unit,
    setelahKlikLupaPw: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = SignInViewModelFactory())
) {
    val errorMessage = loginViewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(errorMessage.value) {
        coroutineScope.launch {
            errorMessage.value?.let { msg ->
                snackbarHostState.showSnackbar(msg)
                loginViewModel.errorMessage.value = null  // Reset error message
            }
        }
    }


    Scaffold(
        topBar = {
            WelcomeTopBar(
                topAppBarText = "Login",
                onNavUp = onNavUp,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { contentPadding ->

            FormAuthScreen(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FormLogin(
                        email = email,
                        setelahKlikLogin = setelahKlikLogin,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = setelahKlikLupaPw,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Lupa Password"
                        )
                    }
                }
            }
        },
        bottomBar = {
            AuthBottomBar() {
                setelahKlikRegister()
            }
        }
    )
}


@Composable
fun FormAuthScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item {
            Spacer(modifier = Modifier.height(44.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                content()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun FormLogin(
    email: String?,
    setelahKlikLogin: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        WelcomeBackText()
        Spacer(modifier = Modifier.height(24.dp))
        val focusRequester = remember { FocusRequester() }
        val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
            mutableStateOf(EmailState(email))
        }
        Email(enable = true, emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }



        Password(
            label = "Password",
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { setelahKlikLogin(emailState.text, passwordState.text) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(value = "Login", onClick = { setelahKlikLogin(emailState.text, passwordState.text) }, enable = emailState.isValid && passwordState.isValid)
    }
}

@Preview
@Composable
fun LoginPreview() {
//    val loginViewModel: LoginViewModel = viewModel(factory = SignInViewModelFactory())
    MainLoginScreen(email= "",setelahKlikLogin = { email, password ->  }, onNavUp = { /*TODO*/ }, setelahKlikRegister = { /*TODO*/ }, setelahKlikLupaPw = {})
}

