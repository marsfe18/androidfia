package com.polstat.luthfiani

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.polstat.luthfiani.Destinations
import com.polstat.luthfiani.Destinations.LOGIN
import com.polstat.luthfiani.Destinations.MAIN
import com.polstat.luthfiani.Destinations.REGISTER
import com.polstat.luthfiani.screen.LoginRoute
import com.polstat.luthfiani.screen.MainRoute
import com.polstat.luthfiani.screen.SignUpRoute
import com.polstat.luthfiani.util.isTokenExpired
import com.polstat.luthfiani.viewModel.UserViewModel
import com.polstat.luthfiani.viewModel.UserViewModelFactory

object Destinations {
    const val LOGIN = "login/{email}"
    const val REGISTER = "register"
    const val HOME_SCREEN = "home"
    const val MAIN = "main"
    const val PROFILE_SCREEN = "profile"
    const val DETAIL_UTASAN = "detail/{id}"
    const val EDIT_PROFILE = "edit_profile"
    const val EDIT_PW = "edit_pw"
    const val FORM_UTASAN = "form_utasan"
}

@Composable
fun HistisNavHost(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val token = sharedPrefs.getString("auth_token", null)
    val isExpired = token?.let { isTokenExpired(it) } ?: true

    val startDestination = if (token.isNullOrEmpty() || isExpired) {
        LOGIN
    } else {
        MAIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = LOGIN,
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) {
            var startingEmail = it.arguments?.getString("email") ?: ""
            if (startingEmail.equals("{email}")) startingEmail = ""
            LoginRoute(
                initialEmail = startingEmail,
                berhasilLogin = { navController.navigate(MAIN) },
                setelahKlikRegister = { navController.navigate(REGISTER) },
                onNavUp = { navController.navigateUp() }) {

            }
        }
        composable(REGISTER) {
            SignUpRoute(
                berhasilRegister = { email ->
                    navController.navigate("login/$email") },
                pindahKeLogin = { navController.navigate(LOGIN) }) {
                navController.navigateUp()
            }
        }
        composable(MAIN) {
            MainRoute {
                navController.navigate(LOGIN)
            }
        }
    }
}