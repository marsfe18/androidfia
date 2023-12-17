package com.polstat.luthfiani.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
//import com.polstat.luthfiani.model.Komen
import com.polstat.luthfiani.model.KomenDto
import com.polstat.luthfiani.model.Utas
import com.polstat.luthfiani.util.AndroidDownloader
import com.polstat.luthfiani.util.convertUriToFile
import com.polstat.luthfiani.util.getMimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleUtasViewModel(private val utasRepository: UtasRepository, private val userRepository: UserRepository) : ViewModel() {

    val message = MutableLiveData<String?>()
    private val _utasan = MutableLiveData<Utas>()
    val utasan: LiveData<Utas> = _utasan

    private val _coment = MutableLiveData<List<KomenDto>>()
    val comment: LiveData<List<KomenDto>> = _coment

    fun getUtasById(id: String, context: Context, gagal: () -> Unit) {
        utasRepository.getUtasById(id, context, success = {
            _utasan.value = it
        }) {
            message.value = it
        }
    }

    fun getComent(id: String, context: Context, gagal: () -> Unit) {
        utasRepository.getComentByUtas(id, context, succes = {
            _coment.value = it
        }, gagal = {
            message.value = it
            gagal()
        })
    }

    fun addComment(id: String, komen:String, file: Uri?,context: Context, gagal: () -> Unit) {
        val mimeType= getMimeType(context,file)
        val files = file?.let { convertUriToFile(it, context) }
        utasRepository.addKomen(id, komen, files, mimeType, context, sukses = {
            _coment.value = it
            getUtasById(id, context, gagal = {
                message.value = "Gagal meload utasan"
                gagal()
            })
        }) {
            message.value = "Gagal menambah komentar $it"
        }
    }

    fun downloadFile(context: Context, dokumenId: Int) = viewModelScope.launch(
        Dispatchers.IO) {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPrefs.getString("auth_token", "") ?: ""
        val downloader = AndroidDownloader(context)
        downloader.downloadFile("http://192.168.0.199:8080/api/download/$dokumenId", token)
    }
}

class SingleUtasViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleUtasViewModel::class.java)) {
            return SingleUtasViewModel(UtasRepository,UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}