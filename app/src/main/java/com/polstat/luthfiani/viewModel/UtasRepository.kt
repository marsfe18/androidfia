package com.polstat.luthfiani.viewModel

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.polstat.luthfiani.model.ErrorResponse
//import com.polstat.luthfiani.model.Komen
import com.polstat.luthfiani.model.KomenDto
import com.polstat.luthfiani.model.KomenRequest
import com.polstat.luthfiani.model.KomenResponse
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.model.UtasRequest
import com.polstat.luthfiani.model.UtasResponse
import com.polstat.luthfiani.util.ApiService
import com.polstat.luthfiani.util.createRetrofit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object UtasRepository {

    fun getUtases(context: Context, berhasil: (List<Utas>) -> Unit, gagal: (String) -> Unit) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUtases() // Pastikan metode ini ada di ApiService
        call.enqueue(object : Callback<UtasResponse> {
            override fun onResponse(call: Call<UtasResponse>, response: Response<UtasResponse>) {
                Log.d("MASUK","REPOSITORY ALL UTASAN ${response.code()}")
                if (response.isSuccessful) {
                    val utases = response.body()?._embedded?.utasDtoes ?: emptyList()
                    berhasil(utases)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    gagal(errorMessage)
                }
            }

            override fun onFailure(call: Call<UtasResponse>, t: Throwable) {
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun getMyUtasan(context: Context, berhasil: (List<Utas>) -> Unit, gagal: (String) -> Unit) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUtasanku() // Pastikan metode ini ada di ApiService
        call.enqueue(object : Callback<UtasResponse> {
            override fun onResponse(call: Call<UtasResponse>, response: Response<UtasResponse>) {
                Log.d("MASUK","REPOSITORY ALL UTASAN ${response.code()}")
                if (response.isSuccessful) {
                    val utases = response.body()?._embedded?.utasDtoes ?: emptyList()
                    berhasil(utases)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    gagal(errorMessage)
                }
            }

            override fun onFailure(call: Call<UtasResponse>, t: Throwable) {
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun addUtasan(context: Context, baru: UtasRequest, files: File, mimeType: String, succesAdd:(id:Int) -> Unit, errorAdd:(String)->Unit) {
        val utasanPart = Gson().toJson(baru).toRequestBody("application/json".toMediaTypeOrNull())
        val requestFile = files.asRequestBody(mimeType.toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", files.name, requestFile)
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.addUtasan(utasanPart, filePart)
        call.enqueue(object : Callback<Utas> {
            override fun onResponse(call: Call<Utas>, response: Response<Utas>) {
                if (response.isSuccessful) {
                    response.body()?.id?.let { succesAdd(it) }
                } else {
//                    Log.d("TESF FILE","createPenugasan repsository ${response.code()} ${response.errorBody()?.string()}")
                    errorAdd("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Utas>, t: Throwable) {
//                Log.d("TESF FILE","createPenugasan repsository gagal panggil api")
                errorAdd("Failure: ${t.message}")
            }
        })
    }

    fun getUtasById(id: String, context: Context, success: (Utas?) -> Unit, gagal: (String) -> Unit) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUtasById(id) // Pastikan metode ini ada di ApiService
        call.enqueue(object : Callback<Utas> {
            override fun onResponse(call: Call<Utas>, response: Response<Utas>) {

                if (response.isSuccessful) {
                    val utases = response.body()
                    success(utases)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    gagal(errorMessage)
                }
            }

            override fun onFailure(call: Call<Utas>, t: Throwable) {
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun getComentByUtas(
        id: String,
        context: Context,
        succes: (List<KomenDto>?) -> Unit,
        gagal: (String) -> Unit
    ) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getKomenUtas(id) // Pastikan metode ini ada di ApiService
        call.enqueue(object : Callback<KomenResponse> {
            override fun onResponse(call: Call<KomenResponse>, response: Response<KomenResponse>) {
//                Log.d("MASUK","REPOSITORY ALL UTASAN ${response.code()}")
                if (response.isSuccessful) {
                    val utases = response.body()?._embedded?.komenDtoes
//                    Log.d("MASUK","REPOSITORY ALL UTASAN ${response.body()}")
                    succes(utases)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    gagal(errorMessage)
                }
            }

            override fun onFailure(call: Call<KomenResponse>, t: Throwable) {
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun addKomen(id: String, komen: String, file: File?, mimeType: String?, context: Context, sukses: (List<KomenDto>?) -> Unit, gagal: (String) -> Unit) {
        val komens = KomenRequest(id, komen)
        val komenPart = Gson().toJson(komens).toRequestBody("application/json".toMediaTypeOrNull())
        val filePart = file?.let {
            val requestFile = it.asRequestBody(mimeType?.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        }
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.addKomen(komenPart, filePart) // Pastikan metode ini ada di ApiService
        call.enqueue(object : Callback<KomenResponse> {
            override fun onResponse(call: Call<KomenResponse>, response: Response<KomenResponse>) {
//                Log.d("MASUK","REPOSITORY ALL UTASAN ${response.code()}")
                if (response.isSuccessful) {
                    val utases = response.body()?._embedded?.komenDtoes
//                    Log.d("MASUK","REPOSITORY ALL UTASAN ${response.body()}")
                    sukses(utases)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    gagal(errorMessage)
                }
            }

            override fun onFailure(call: Call<KomenResponse>, t: Throwable) {
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun deleteUtas(context: Context, it: Int, sukses: (String?) -> Unit, gagal: (String) -> Unit) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.deleteUtas(it)
        call.enqueue(object : Callback<ErrorResponse> {
            override fun onResponse(call: Call<ErrorResponse>, response: Response<ErrorResponse>) {
                Log.d("MASUK","REPOSITORY ALL UTASAN ${response.code()}")
                if (response.isSuccessful) {
                    val utases = response.body()?.message
                    Log.d("MASUK","REPOSITORY ALL UTASAN ${response.body()}")
                    sukses(utases)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    gagal(errorMessage)
                }
            }
            override fun onFailure(call: Call<ErrorResponse>, t: Throwable) {
                gagal(t.message ?: "Unknown error occurred")
            }
        })
    }

    fun edit(
        context: Context,
        id: Int,
        edit: UtasRequest,
        succesAdd: (String) -> Unit,
        errorAdd: (String) -> Unit
    ) {
        val retrofit = createRetrofit(context)
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.editUtas(id,edit)
        call.enqueue(object : Callback<Utas> {
            override fun onResponse(call: Call<Utas>, response: Response<Utas>) {
                Log.d("RESPONSE", "RESPONSE : ${response.code()} ${response.errorBody()}")
                if (response.isSuccessful) {
                    response.body()?.let { succesAdd("Berhasil") }
                } else {
//                    Log.d("TESF FILE","createPenugasan repsository ${response.code()} ${response.errorBody()?.string()}")
                    errorAdd(response.message())
                }
            }

            override fun onFailure(call: Call<Utas>, t: Throwable) {
//                Log.d("TESF FILE","createPenugasan repsository gagal panggil api")
                errorAdd("Failure: ${t.message}")
            }
        })
    }
}
