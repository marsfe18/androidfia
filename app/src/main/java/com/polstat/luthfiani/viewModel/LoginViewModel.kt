package com.polstat.luthfiani.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    val isAuthenticated = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()


    fun saveToken(context: Context, token: String, email:String) {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString("auth_token", token)
            putString("email", email)
            apply()
        }
    }

    fun signIn(
        email: String,
        password: String,
        context: Context,
        onSignInComplete: () -> Unit,
    ) {
        userRepository.login(
            email,
            password,
            berhasilLogin = { token ->
                saveToken(context,token, email)
                onSignInComplete()
                            },
            gagalLogin = {
                errorMessage.value= "Password/email salah"
            }
        )
    }
}

class SignInViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
