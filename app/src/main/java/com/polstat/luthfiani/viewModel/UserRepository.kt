package com.polstat.luthfiani.viewModel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polstat.luthfiani.model.EditRequest
import com.polstat.luthfiani.model.ErrorResponse
import com.polstat.luthfiani.model.PasswordRequest
import com.polstat.luthfiani.model.RegisterRequest
import com.polstat.luthfiani.model.RegisterResponse
import com.polstat.luthfiani.model.UserProfile
import com.polstat.luthfiani.util.ApiService
import com.polstat.luthfiani.util.LoginRequest
import com.polstat.luthfiani.util.LoginResponse
import com.polstat.luthfiani.util.createRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRepository {
    private val _user = MutableLiveData<UserProfile?>(
        UserProfile(
            namaDepan = "Luthfiani",
            namaBelakang = "Nur Aisyah",
            email = "luthfiani@mail.com",
            role = "USER",
            username = "luthfia"
        )
    )
    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username
    val user: LiveData<UserProfile?>
        get() = _user

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.199:8080/") // Replace with your base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun login(email: String, password: String, berhasilLogin: (token: String) -> Unit, gagalLogin: (String) -> Unit) {
        val call = apiService.login(LoginRequest(email, password))
        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.access_token
                    Log.d("TOKEN", "$token")
                    token?.let { berhasilLogin(it) }
                } else {
                    Log.d(ContentValues.TAG,"Login : ${response.errorBody()?.toString()}")
                    response.errorBody()?.let { gagalLogin(it.string()) }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginError", "Login failed: ${t.message}", t)
            }
        })
    }

    fun register(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        berhasil: (token: String) -> Unit,
        gagal: (String) -> Unit
    ) {
        val call = apiService.register(RegisterRequest(firstname, lastname, email, password))
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.access_token
                    Log.d("TOKEN", "Register success with token: $token")
                    token?.let { berhasil(it) }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    Log.d("RegisterError", "Register failed: $errorMessage")
                    gagal(errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("RegisterError", "Register failed: ${t.message}", t)
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun loadUserData(context: Context, berhasil: (UserProfile?) -> Unit, gagal: (String) -> Unit) {
        Log.d(ContentValues.TAG, "MEMANGGIL getProfile USERREPOSIROTY")
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        apiService.getProfile().enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    _user.value = profile
                    berhasil(profile)
                } else {
                    response.errorBody()?.let { gagal(it.string()) }
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                t.message?.let { gagal(it) }
            }
        })
    }

    fun editProfile(
        firstname: String,
        lastname: String,
        email: String,
        context: Context,
        berhasil: () -> Unit,
        gagal: (String) -> Unit
    ) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.editProfile(EditRequest(firstname, lastname, email))
        call.enqueue(object : Callback<UserProfile> {
            override fun onResponse(
                call: Call<UserProfile>,
                response: Response<UserProfile>
            ) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                    berhasil()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    Log.d("RegisterError", "Register failed: $errorMessage")
                    gagal("Gagal mengedit data")
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                Log.e("RegisterError", "Register failed: ${t.message}", t)
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun editPasssword(context:Context, newPassword: String, oldPassword: String,gagal: (String) -> Unit, berhasil: () -> Unit) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.editProfile(PasswordRequest(oldPassword, newPassword, newPassword))
        call.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(
                call: Call<ErrorResponse>,
                response: Response<ErrorResponse>
            ) {
                if (response.isSuccessful) {
                    berhasil()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    Log.d("RegisterError", "Register failed: $errorMessage")
                    gagal("Gagal mengedit password")
                }
            }

            override fun onFailure(call: Call<ErrorResponse>, t: Throwable) {
                Log.e("RegisterError", "Register failed: ${t.message}", t)
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

}

