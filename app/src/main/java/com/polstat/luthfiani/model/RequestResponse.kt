package com.polstat.luthfiani.model

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val access_token: String, val refresh_token: String)
data class ErrorResponse(
    val status: String,
    val message: String,
    val waktu: String

)

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val access_token: String,
    val refresh_token: String
)

data class UserProfile(
    val namaDepan: String,
    val namaBelakang: String,
    val email: String,
    val password: String? = null, // Note: Storing passwords on the client side is not recommended.
    val role: String,
    val enabled: Boolean? = null,
    val authorities: List<Authority>? = null,
    val username: String,
    val accountNonLocked: Boolean? = null,
    val credentialsNonExpired: Boolean? = null,
    val accountNonExpired: Boolean? = null
)

data class Authority(
    val authority: String
)

data class EditRequest(
    val namaDepan: String,
    val namaBelakang: String,
    val email: String
)

data class PasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmationPassword: String
)

data class UtasRequest(
    val judul: String,
    val topik: String,
    val isi: String
)

data class KomenResponse(
    val _embedded: KomenEmbedded,
    val _links: Links
)

data class KomenEmbedded(
    val komenDtoes: List<KomenDto>
)

data class KomenDto(
    val id: Int,
    val utas: Int,
    val isi: String,
    val waktu: String,
    val user: String,
    val role: String,
    val doc: String?,
    val _links: KomenLinks
)

data class KomenLinks(
    val self: Link,
    val user: Link,
    val file: Link,
    val utas: Link,
    val reply: Link
)

data class Links(
    val self: Link
)

data class KomenRequest(
    val utas: String,
    val isi:String
)