package com.polstat.luthfiani.model

data class Utas(
    val id: Int,
    val judul: String,
    val isi: String,
    val topik: Int,
    val doc: Int,
    val user: String,
    val role: String,
    val waktu: String,
    val _links: UtasLinks
)

data class UtasLinks(
    val self: Link,
    val user: Link,
    val file: Link,
    val topik: Link,
    val allUtasan: Link,
    val komen: Link
)

data class Link(
    val href: String
)


data class UtasResponse(
    val _embedded: UtasEmbedded
)

data class UtasEmbedded(
    val utasDtoes: List<Utas>
)

fun GetTopik(num: Int): String {
    return when(num) {
        0 -> "keluhan"
        1 -> "Curhatan"
        else -> { "Unkown" }
    }
}