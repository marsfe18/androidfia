package com.polstat.luthfiani.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.model.UtasRequest
import com.polstat.luthfiani.util.convertUriToFile

class UtasViewModel(private val utasRepository: UtasRepository, private val userRepository: UserRepository) : ViewModel() {

    val message = MutableLiveData<String?>()
    private val _utasan = MutableLiveData<List<Utas>>(listOf())
    val utasan: LiveData<List<Utas>> = _utasan

    private val _toEditUtas = MutableLiveData<Utas?>()
    val toEditUtas: LiveData<Utas?> = _toEditUtas

    private val _detailUtas = MutableLiveData<Utas?>()
    val detailUtas: LiveData<Utas?> = _detailUtas

    private val _myUtasan = MutableLiveData<List<Utas>>(listOf())
    val myUtasan: LiveData<List<Utas>> = _myUtasan

    fun loadAllUtasan(context: Context) {
        Log.d("MASUK","LOAD ALL UTASAN")
        utasRepository.getUtases(context, berhasil = {
            _utasan.value = it
        }, gagal = {
            message.value = "Gagal meload data utasan"
        })
    }

    fun loadMyUtasan(context: Context) {
        Log.d("MASUK","LOAD MY UTASAN")
        utasRepository.getMyUtasan(context, berhasil = {
            _myUtasan.value = it
        }, gagal = {
            message.value = "Gagal meload data utasan"
        })
    }

    fun clearMessage() {
        message.value = null
    }

    fun addUtasan(
        judul: String,
        topik: String,
        isi: String,
        file: Uri?,
        mimeType: String,
        context: Context,
        successTambah: (id: Int) -> Unit
    ){
        val baru = UtasRequest(
            judul, topik, isi
        )
        val files = file?.let { convertUriToFile(it, context) }
        Log.d("ADD UTASAN", "UTASAN $baru $file")
        if (files != null) {
            utasRepository.addUtasan(context,baru,files, mimeType,succesAdd = {
                message.value = "Berhasil tambah"
                successTambah(it)
            }, errorAdd = {
                message.value = "Kesalahan $it"
            })
        }
    }

    fun delete(context: Context, it: Int, gagal:() -> Unit) {
        Log.d("HALOOOOO", "UJI COBA DELETEEEEEEEEEE")
        utasRepository.deleteUtas(context, it, sukses = {
            message.value = it
            loadAllUtasan(context)
        }) {
            message.value = it
            gagal()
        }
    }

    fun editUtas(
        id: Int,
        judul: String,
        topik: String,
        isi: String,
        context: Context,
        successTambah: (id: Int) -> Unit
    ) {
        val edit = UtasRequest(
            judul, topik, isi
        )
        Log.d("ADD UTASAN", "UTASAN $edit")
        utasRepository.edit(context,id,edit, succesAdd = {
            message.value = "Berhasil Edit"
            successTambah(id)
        }, errorAdd = {
            message.value = "Berhasil gagal edit : $it"
        })
    }


}

class UtasViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UtasViewModel::class.java)) {
            return UtasViewModel(UtasRepository,UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}