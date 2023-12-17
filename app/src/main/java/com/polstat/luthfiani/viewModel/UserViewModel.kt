package com.polstat.luthfiani.viewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polstat.luthfiani.model.UserProfile

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _user = MutableLiveData<UserProfile>()
    val user: LiveData<UserProfile>
        get() = _user

    fun clearMessageError() {
        _errorMessage.value = null
    }

    fun deleteToken(context: Context) {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            remove("auth_token")
            remove("email")
            apply()
        }
    }

    fun loadUserData(context: Context) {
        userRepository.loadUserData(context, berhasil = {
            _user.value = it
        }, gagal = {
            _errorMessage.value = it
        })
    }

    fun logout(context: Context, logout:() -> Unit) {
        deleteToken(context)
        logout()
    }

    fun editAkun(
        email: String,
        firstname: String,
        lastname: String,
        context: Context,
        editBerhasil: () -> Unit
    ) {
        userRepository.editProfile(firstname, lastname, email, context, gagal = {
            _errorMessage.value = it
        }, berhasil = editBerhasil)
    }

    fun editPassword(context: Context,newPassword: String, oldPassword: String, berhasil: () -> Unit) {
        userRepository.editPasssword(context,newPassword, oldPassword,gagal = {
            _errorMessage.value = it
        }, berhasil = berhasil)
    }
}

class UserViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



data class User(
    val nama: String,
    val email: String,
    val kelas: String
)
