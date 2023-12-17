package com.polstat.luthfiani.util

import android.content.Context
import com.polstat.luthfiani.model.EditRequest
import com.polstat.luthfiani.model.ErrorResponse
import com.polstat.luthfiani.model.KomenResponse
import com.polstat.luthfiani.model.PasswordRequest
import com.polstat.luthfiani.model.RegisterRequest
import com.polstat.luthfiani.model.RegisterResponse
import com.polstat.luthfiani.model.UserProfile
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.model.UtasRequest
import com.polstat.luthfiani.model.UtasResponse
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File


interface ApiService {
    @POST("/auth/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @POST("/auth/signup")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
    @GET("/api/profil")
    fun getProfile(): Call<UserProfile>
    @GET("/api/utas")
    fun getUtases(): Call<UtasResponse>
    @GET("/api/utasanku")
    fun getUtasanku(): Call<UtasResponse>
    @PATCH("/api/ubah-profil")
    fun editProfile(@Body editRequest: EditRequest): Call<UserProfile>
    @PATCH("/api/ubah-password")
    fun editProfile(@Body editpassword: PasswordRequest): Call<ErrorResponse>
    @Multipart
    @POST("/api/utas")
    fun addUtasan(
        @Part("utas") utasanPart: RequestBody,
        @Part file: MultipartBody.Part): Call<Utas>

    @GET("/api/utas/{id}")
    fun getUtasById(@Path("id") id: String): Call<Utas>
    @GET("/api/utas/{id}/komen")
    fun getKomenUtas(@Path("id") id: String): Call<KomenResponse>

    @Multipart
    @POST("/api/komen")
    fun addKomen(
        @Part("komen") komenPart: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<KomenResponse>

    @DELETE("/api/utas/{id}")
    fun deleteUtas(@Path("id") id: Int): Call<ErrorResponse>

    @PATCH("/api/utas/{id}")
    fun editUtas(
        @Path("id") id: Int,
        @Body utas: UtasRequest,
    ): Call<Utas>

}


data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val access_token: String, val refresh_token: String)

fun createRetrofit(context: Context): Retrofit {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(BearerTokenInterceptor(context))
        .build()

    return Retrofit.Builder()
        .baseUrl("http://192.168.184.38:8080/") // Sesuaikan dengan base URL Anda
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


class BearerTokenInterceptor(context: Context) : Interceptor {
    private val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getString("auth_token", "") ?: ""
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}



