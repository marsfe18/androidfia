package com.polstat.luthfiani.util

import com.auth0.android.jwt.JWT

fun isTokenExpired(token: String): Boolean {
    return try {
        val jwt = JWT(token)
        jwt.isExpired(0)
    } catch (e: Exception) {
        true // Jika token tidak valid, anggap telah kedaluwarsa
    }
}
