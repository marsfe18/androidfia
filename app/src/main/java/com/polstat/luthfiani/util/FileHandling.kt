package com.polstat.luthfiani.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

fun convertUriToFile(uri: Uri, context: Context): File? {
    // Mendapatkan nama file dari URI (jika tidak bisa, gunakan timestamp atau nama unik)
    val fileName = uri.lastPathSegment ?: System.currentTimeMillis().toString()

    // Mendapatkan direktori cache aplikasi untuk menyimpan file sementara
    val tempFile = File(context.cacheDir, fileName)

    try {
        // Mendapatkan input stream dari URI menggunakan ContentResolver
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            // Membuat output stream ke file yang akan dibuat
            FileOutputStream(tempFile).use { outputStream ->
                // Menyalin data dari input stream ke output stream
                val buffer = ByteArray(1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
            }
        }
    } catch (e: Exception) {
        // Menangani eksepsi, misalnya mencatat error atau memberi tahu pengguna
        return null
    }

    return tempFile
}

fun getMimeType(context: Context, uri: Uri?): String? {
    return if (uri == null) null
    else context.contentResolver.getType(uri)
}

fun getFileNameFromUri(context: Context, uri: Uri?): String {
    var fileName: String? = null
    uri?.let { returnUri ->
        context.contentResolver.query(returnUri, null, null, null, null)
    }?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }
    fileName?.let { return it } ?: run {
        return "Pilih file"
    }
}