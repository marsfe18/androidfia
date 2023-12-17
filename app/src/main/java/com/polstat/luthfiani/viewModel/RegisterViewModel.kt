package com.polstat.luthfiani.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String?>()

    fun register(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        context: Context,
        onSignInComplete: (String) -> Unit,
    ) {
        userRepository.register(
            firstname, lastname, email, password, berhasil = { token ->
                onSignInComplete(email)
            },
            gagal = {
                errorMessage.value = it
            }
        )
    }

    fun signInAsGuest(
        onSignInComplete: () -> Unit,
    ) {

        onSignInComplete()
    }
}

class RegisterModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
