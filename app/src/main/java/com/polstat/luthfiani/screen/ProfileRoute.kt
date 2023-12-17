package com.polstat.luthfiani.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.polstat.luthfiani.R
import com.polstat.luthfiani.components.CircularButton
import com.polstat.luthfiani.components.CustomButton
import com.polstat.luthfiani.components.InformationCard
import com.polstat.luthfiani.components.ProfileImage
import com.polstat.luthfiani.viewModel.UserViewModel
import com.polstat.luthfiani.viewModel.UserViewModelFactory

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory()),
    navController: NavController = rememberNavController(),
    klikEditProfile: () -> Unit,
    klikEditPw: () -> Unit,
    logout: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        userViewModel.loadUserData(context)
    }
    val klikHapusAkun = {

    }
    val userProfile = userViewModel.user.observeAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 32.dp, horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userProfile.value?.let {
            ProfileImage(R.drawable.dosen)
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)) {
                    InformationCard("Firstname", it.namaDepan)
                }
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)) {
                    InformationCard("Lastname", it.namaBelakang)
                }
            }
            InformationCard("Email", it.email)
            InformationCard("Role", it.role)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CircularButton(icon = Icons.Default.Edit, onClick = { klikEditProfile() }, description = "Edit Profile")
                CircularButton(icon = Icons.Default.VpnKey, onClick = { klikEditPw() }, description = "Edit Password")
                CircularButton(icon = Icons.Default.Delete, onClick = { klikHapusAkun() }, description = "Delete Account")
            }
//            Spacer(modifier = Modifier.weight(1f))

        }
        CustomButton(value = "Logout", onClick = { userViewModel.logout(context) { logout() }
        })
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevProfil() {
    ProfileScreen(
        klikEditProfile = {},
        klikEditPw = {}
    ) {

    }
}