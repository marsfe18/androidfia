package com.polstat.luthfiani.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.polstat.luthfiani.Destinations.DETAIL_UTASAN
import com.polstat.luthfiani.Destinations.EDIT_PROFILE
import com.polstat.luthfiani.Destinations.EDIT_PW
import com.polstat.luthfiani.Destinations.FORM_UTASAN
import com.polstat.luthfiani.Destinations.HOME_SCREEN
import com.polstat.luthfiani.Destinations.PROFILE_SCREEN
import com.polstat.luthfiani.components.BottomBarApp
import com.polstat.luthfiani.components.BottomNavItems
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.screen.utasan.DetailRoute
//import com.polstat.luthfiani.screen.utasan.DetailScreen
import com.polstat.luthfiani.screen.utasan.FormUtasanScreen
import com.polstat.luthfiani.viewModel.UserViewModel
import com.polstat.luthfiani.viewModel.UserViewModelFactory

@Composable
fun MainRoute(
    navController: NavHostController = rememberNavController(),
    logout: () -> Unit
) {
    MainScreen(navController = navController ,logout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    logout: () -> Unit
) {
    var navigationSelectedItem by rememberSaveable {
        mutableStateOf(0)
    }
    var pageNumber by rememberSaveable {
        mutableStateOf(0)
    }
    var onDetailScreen by rememberSaveable {
        mutableStateOf(false)
    }
    var editUtasan by remember { mutableStateOf<Utas?>(null) }
    var judulBar by remember { mutableStateOf("Home") }
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    Scaffold(
        bottomBar = {
            if(!onDetailScreen) BottomBarApp(navigationSelectedItem,navController) { navigationSelectedItem = it }
        },
        topBar = {
            TopAppBar(title = { Text(text = judulBar) })
        }
    ) {padding ->
        NavHost(
            navController = navController,
            startDestination = HOME_SCREEN,
            modifier = Modifier.padding(paddingValues = padding),
            builder = {
                composable(HOME_SCREEN) {
                    judulBar = "Home"
                    onDetailScreen = false
                    HomeScreen(
                        pageNumber = pageNumber,
                        navController = navController,
                        gantiPage = { pageNumber = it },
                        setelahKlikDetail = {id ->
                            navController.navigate("detail/$id") },
                        createUtasan = {
                            navController.navigate(FORM_UTASAN)
                        },
                        editUtasan = {
                            editUtasan = it
                            navController.navigate(FORM_UTASAN)
                        }
                    )
                }
                composable(PROFILE_SCREEN) {
//                    onDetailScreen = false
                    judulBar = "Account"
                    ProfileScreen(userViewModel,
                        navController,
                        klikEditProfile = { navController.navigate(EDIT_PROFILE) },
                        klikEditPw = { navController.navigate(EDIT_PW) },logout)
                }
                composable(EDIT_PROFILE) {
//                    onDetailScreen = false
                    judulBar = "Edit Profile"
                    EditProfileRoute(userViewModel,setelahEdit = {
                        navController.navigate(PROFILE_SCREEN)
                    })
                }
                composable(EDIT_PW) {
//                    onDetailScreen = false
                    judulBar = "Edit Password"
                    EditPwRoute {
                        navController.navigate(PROFILE_SCREEN)
                    }
                }
                composable(FORM_UTASAN) {
//                    onDetailScreen = false
                    judulBar = "Utasan"
                    FormUtasanScreen(editUtasan ,successTambah = {
                        editUtasan = null
                        navController.navigate("detail/$it")
                    }, onBack = {
                        editUtasan = null
                        navController.navigateUp()
                    })
                }
                composable(DETAIL_UTASAN,arguments = listOf(navArgument("id") { defaultValue = "" })) {
                    onDetailScreen = true
                    judulBar = "Detail Utasan"
                    val utasanId = it.arguments?.getString("id") ?: ""
                    DetailRoute(id = utasanId, onback = {
                        onDetailScreen = false
                        navController.navigate(HOME_SCREEN)
                    }) {
                        navController.navigate(HOME_SCREEN)
                    }
                }
            }
        )
    }
}

@Composable
fun PageDetail() {
    TODO("Not yet implemented")
}

@Preview
@Composable
fun PrevMain() {
    MainScreen(navController = rememberNavController()) {

    }
}
