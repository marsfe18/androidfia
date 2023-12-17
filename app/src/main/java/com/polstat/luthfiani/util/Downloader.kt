package com.polstat.luthfiani.util

interface Downloader {
    fun downloadFile(url: String, token: String): Long
}